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
let uploadedImages = []; // 이미지 정보를 저장할 배열

async function insertImage(event) {
  if (!event.target.files || event.target.files.length === 0) {
    console.log("파일이 선택되지 않았습니다.");
    return;
  }

  let name = encodeURIComponent(event.target.files[0].name);
  let result = await fetch('/presigned-url?filename=' + name);
  result = await result.text();
  console.log("업로드된 presigned URL:", result);

  const file = event.target.files[0];
  let uploadResponse = await fetch(result, {
    method: 'PUT',
    body: file
  });

  if (uploadResponse.ok) {
    let uploadedUrl = result.split("?")[0]; // ? 이전까지의 URL만 저장

    // 임시 ID 부여 (서버에서 실제 ID를 제공하면 변경 가능)
    let imageId = Date.now(); // 현재 시간을 ID로 사용 (예제)
    uploadedImages.push({ id: imageId, url: uploadedUrl });

    console.log("저장된 이미지 정보:", uploadedImages);

    // 미리보기 추가
    const img = document.createElement("img");
    img.src = URL.createObjectURL(file); // 로컬 미리보기
    img.style.maxWidth = "100%";
    img.setAttribute("data-image-id", imageId); // 이미지 ID 저장
    contentArea.appendChild(img);
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

  if (!title.trim() || !content.trim()) {
    alert("제목과 내용을 모두 입력해주세요.");
    return;
  }

  // 서버에 보낼 데이터 준비
  const postData = {
    title,
    content,
    images: uploadedImages, // 저장된 이미지 리스트
  };

  console.log("전송할 데이터:", postData);

  fetch('/save-post', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(postData),
  })
    .then(response => response.json())
    .then(data => {
      if (data.status === "success") {
        alert("글이 성공적으로 저장되었습니다!");
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
