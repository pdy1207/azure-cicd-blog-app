 // 스크롤 진행률을 계산하여 프로그레스 바 업데이트
    function updateProgressBar() {
      const scrollTop = window.scrollY;
      const docHeight =
        document.documentElement.scrollHeight - window.innerHeight;
      const scrollPercent = (scrollTop / docHeight) * 100;

      document.getElementById("progress-bar").style.width = scrollPercent + "%";
    }

    // 스크롤 이벤트 감지
    window.addEventListener("scroll", updateProgressBar);

    function showReplyInput(button) {
      console.log("🚀 ~ showReplyInput ~ button:", button);
      // 기존의 답글 입력창 제거 (하나만 유지)
      document.querySelectorAll(".reply_input").forEach((el) => el.remove());

      // 현재 댓글 요소 찾기
      const comment = button.closest(".comment");

      // 기존 댓글 입력창과 동일한 HTML 추가
      const replyInputHtml = `
    <div class="comment_input reply_input">
      <textarea placeholder="댓글을 입력하세요."></textarea>
      <button onclick="addReply(this)">등록</button>
    </div>
  `;

      // 해당 댓글 아래에 추가
      comment.insertAdjacentHTML("afterend", replyInputHtml);
    }

    function addReply(button) {
      const replyInputDiv = button.closest(".reply_input");
      const text = replyInputDiv.querySelector("textarea").value.trim();

      if (!text) {
        alert("답글을 입력하세요.");
        return;
      }

      // 새로운 답글 HTML
      const replyHtml = `
    <li class="animation_up">
      <div class="comment">
        <a class="link_profile">
          <img class="img_thumb" alt="프로필 이미지" height="32px" src="https://via.placeholder.com/32" width="32px"/>
        </a>
        <div class="cont_info">
          <div class="info_append">
            <div class="tit_userid">
              <a class="link_userid" href="#">사용자</a>
            </div>
            <span class="txt_time">${new Date().toLocaleDateString()}</span>
          </div>
          <p class="desc_comment">${text}</p>
          <button class="btn_reply" onclick="showReplyInput(this)">답글달기</button>
        </div>
      </div>
    </li>
  `;

      // 댓글 리스트에 추가
      let replyList = replyInputDiv.previousElementSibling.nextElementSibling;
      if (!replyList || !replyList.classList.contains("wrap_reply")) {
        replyList = document.createElement("div");
        replyList.classList.add("wrap_reply");
        replyList.innerHTML = '<ul class="list_reply"></ul>';
        replyInputDiv.previousElementSibling.after(replyList);
      }

      replyList.querySelector(".list_reply").innerHTML += replyHtml;

      // 입력창 제거
      replyInputDiv.remove();
    }

