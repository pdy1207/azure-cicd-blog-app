 function checkPasswordMatch() {
                var password = document.getElementById("txt_password").value;
                var confirmPassword = document.getElementById("txt_password2").value;
                var message = document.getElementById("password_match_message");

                if (password === "" || confirmPassword === "") {
                    message.innerHTML = "특수문자(예: !@#$ 등) 1자 이상을 포함한 10~15 글자의 비밀번호입니다.";
                    message.style.color = "#000"; // 기본 색상
                    return false; // 비어 있으면 제출을 막음
                } else if (password !== confirmPassword) {
                    message.innerHTML = "비밀번호가 일치하지 않습니다.";
                    message.style.color = "red"; // 불일치 시 빨간색
                    return false; // 불일치 시 제출을 막음
                } else {
                    message.innerHTML = "비밀번호가 일치합니다.";
                    message.style.color = "green"; // 일치 시 초록색
                    return true; // 비밀번호가 일치하면 제출 가능
                }
            }

            // 폼을 제출할 때 비밀번호 확인이 유효한지 체크
            document.querySelector("form").addEventListener("submit", function(event) {
                if (!checkPasswordMatch()) {
                    event.preventDefault(); // 비밀번호 확인이 잘못된 경우 제출 막음
                }
            });