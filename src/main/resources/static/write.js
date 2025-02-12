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
function insertImage(event) {
  const file = event.target.files[0];
  if (file) {
    const reader = new FileReader();
    reader.onload = function (e) {
      const img = document.createElement("img");
      img.src = e.target.result;
      img.style.maxWidth = "100%"; // 이미지를 최대 너비로 제한
      contentArea.appendChild(img); // 텍스트 영역에 이미지 삽입
    };
    reader.readAsDataURL(file);
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
