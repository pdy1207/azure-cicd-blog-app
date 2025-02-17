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

document.addEventListener('DOMContentLoaded', function() {
  const publishButton = document.querySelector('.publish-button2');
  if (publishButton) {
    publishButton.addEventListener('click', function() {
      const titleInput = document.querySelector('.title-input');
      const contentArea = document.querySelector('.content-area');
      const title = titleInput.value;
      let content = contentArea.innerHTML;

      // <div><br></div> 태그가 3개 이상인지 확인
      const brDivCount = (content.match(/<div><br><\/div>/g) || []).length;

      // <div><br></div>가 하나 또는 여러 개만 있는 경우 제거
      content = content.replace(/<div><br><\/div>/g, "").trim();

      // <img> 태그 제거
      content = content.replace(/<img[^>]*>/g, "").trim();

      // 제거 후 여전히 빈 내용이라면 경고
      if (!title.trim() || !content.trim()) {
        alert("제목과 내용을 모두 입력해주세요.");
        return;
      }

      // <div><br></div> 태그가 3개 이상일 경우에는 그대로 보내고, 그렇지 않으면 빈 태그를 제거한 내용으로 전송
      let processedContent = content;
      if (brDivCount >= 3) {
        processedContent = content; // 그대로 보내는 경우
      }

      // 게시글의 id 값을 URL에서 가져오기
      const postId = window.location.pathname.split('/').pop();

      // 서버에 보낼 데이터 준비
      const postData = {
        title,
        content: processedContent, // 처리된 본문 (이미지 제거된 내용)
      };

      fetch(`/edit-post/${postId}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(postData),
      })
      .then(response => {
        if (!response.ok) {
          throw new Error('서버 오류 발생');
        }
        return response.json();
      })
      .then(data => {
        if (data.status === "success") {
          alert("글이 성공적으로 수정되었습니다!");
          window.location.href = `/detail-list/${postId}`; // 수정 후 홈으로 이동
        } else {
          alert("글 수정에 실패했습니다.");
        }
      })
    });
  }
});
