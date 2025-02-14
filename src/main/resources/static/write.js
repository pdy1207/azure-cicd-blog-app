const contentArea = document.querySelector(".content-area");

// 버튼 클릭 시 스타일 적용
document.getElementById("boldButton").addEventListener("click", () => {
  document.execCommand("bold");
});

document.getElementById("italicButton").addEventListener("click", () => {
  document.execCommand("italic");
});

document.getElementById("underlineButton").addEventListener("click", () => {
  document.execCommand("underline");
});

// 이미지 첨부 버튼 클릭 시 파일 선택 창 띄우기
document.getElementById("imageButton").addEventListener("click", () => {
  document.getElementById("imageUpload").click();
});

// 글씨 크기 변경
document
  .getElementById("fontSizeSelect")
  .addEventListener("change", (event) => {
    const selectedSize = event.target.value;
    document.execCommand("fontSize", false, 7); // 임시로 fontSize 명령어 사용
    const currentSelection = window.getSelection();
    const range = currentSelection.getRangeAt(0);
    const selectedElement = range.startContainer.parentNode;
    selectedElement.style.fontSize = selectedSize; // 선택된 텍스트에 폰트 크기 적용
  });

// 이미지 삽입 함수
async function insertImage(event) {
  // 파일이 선택되지 않았을 경우 처리
  if (!event.target.files || event.target.files.length === 0) {
    console.log("파일이 선택되지 않았습니다.");
    return; // 함수 종료
  }

  let name = encodeURIComponent(event.target.files[0].name);

  // presigned URL을 가져오는 요청
  let result = await fetch('/presigned-url?filename=' + name);
  result = await result.text();
  console.log(result);

  // presigned URL로 파일을 PUT 방식으로 업로드
  const file = event.target.files[0];
  let uploadResponse = await fetch(result, {
    method: 'PUT',
    body: file
  });

  console.log(uploadResponse);

  // PUT 요청 성공 시 이미지 URL을 설정
  if (uploadResponse.ok) {
    let uploadedUrl = result.split("?")[0]; // presigned URL에서 실제 URL만 추출

    // 새로운 img 요소 생성 후 src 설정
//    const uploadedImg = document.createElement('img');
//    uploadedImg.src = uploadedUrl;  // 업로드된 이미지 URL 설정
//    uploadedImg.alt = "Uploaded Image";
//    uploadedImg.style.maxWidth = "100%"; // 이미지를 최대 너비로 제한
//    document.body.appendChild(uploadedImg); // 페이지에 이미지 삽입

    // 이미지 삽입 (미리보기용)
    const reader = new FileReader();
    reader.onload = function (e) {
      const img = document.createElement("img");
      img.src = e.target.result; // 미리보기 이미지를 삽입
      img.style.maxWidth = "100%"; // 이미지를 최대 너비로 제한
      contentArea.appendChild(img); // 텍스트 영역에 이미지 삽입
    };
    reader.readAsDataURL(file);
  } else {
    console.error("이미지 업로드 실패");
  }
}




// 키보드 이벤트로 단축키 적용 (Ctrl + B, Ctrl + I, Ctrl + U)
contentArea.addEventListener("keydown", (event) => {
  if (event.ctrlKey) {
    if (event.key === "b") {
      document.execCommand("bold");
      event.preventDefault(); // 기본 동작 방지
    } else if (event.key === "i") {
      document.execCommand("italic");
      event.preventDefault();
    } else if (event.key === "u") {
      document.execCommand("underline");
      event.preventDefault();
    }
  }
});

document.querySelector('.publish-button').addEventListener('click', function() {
  const title = document.querySelector('.title-input').value;
  const content = document.querySelector('.content-area').innerHTML;

  // 글 제목과 본문 내용이 비어있는지 확인
  if (!title.trim() || !content.trim()) {
    alert("제목과 내용을 모두 입력해주세요.");
    return;
  }

  // 서버에 데이터 전송 (AJAX 사용)
  fetch('/save-post', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({ title: title, content: content }),
  })
    .then(response => response.json())
    .then(data => {
      if (data.success) {
        alert("글이 성공적으로 저장되었습니다!");
        // 성공적으로 저장되면 다른 페이지로 이동할 수도 있습니다.
        window.location.href = '/';
      } else {
        alert("글 저장에 실패했습니다.");
      }
    })
    .catch(error => {
      console.error("오류 발생:", error);
      alert("서버 오류입니다. 나중에 다시 시도해주세요.");
    });
});
