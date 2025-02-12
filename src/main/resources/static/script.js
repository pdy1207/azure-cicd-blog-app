/* 스크롤 */
let lastScrollY = window.scrollY;
const header = document.querySelector("header");
const showThreshold = 200; // 200px 이상 내려가면 숨김

window.addEventListener("scroll", () => {
  const currentScrollY = window.scrollY;

  header.style.boxShadow = "0 2px 10px rgba(0, 0, 0, 0.1)";
  if (currentScrollY < lastScrollY) {
    // 스크롤 올리면 다시 표시
    header.style.transform = "translateY(0)";
  } else if (currentScrollY > showThreshold) {
    // 200px 이상 내려가면 숨김
    header.style.transform = "translateY(-100%)";
  }

  if (currentScrollY === 0) {
    header.style.boxShadow = "none";
  }

  lastScrollY = currentScrollY;
});
/* 스크롤 */

$("#carousel div").click(function () {
  window.open("/detail", "_blank");
});

/* 캐러셀 */
function moveToSelected(element) {
  if (element == "next") {
    var selected = $(".selected").next();
  } else if (element == "prev") {
    var selected = $(".selected").prev();
  } else {
    var selected = element;
  }

  var next = $(selected).next();
  var prev = $(selected).prev();
  var prevSecond = $(prev).prev();
  var nextSecond = $(next).next();

  $(selected).removeClass().addClass("selected");

  $(prev).removeClass().addClass("prev");
  $(next).removeClass().addClass("next");

  $(nextSecond).removeClass().addClass("nextRightSecond");
  $(prevSecond).removeClass().addClass("prevLeftSecond");

  $(nextSecond).nextAll().removeClass().addClass("hideRight");
  $(prevSecond).prevAll().removeClass().addClass("hideLeft");
}

$(document).ready(function () {
  moveToSelected($(".selected"));
});

$(document).keydown(function (e) {
  switch (e.which) {
    case 37: // left
      moveToSelected("prev");
      break;

    case 39: // right
      moveToSelected("next");
      break;

    default:
      return;
  }
  e.preventDefault();
});

$("#carousel div").click(function () {
  moveToSelected($(this));
});

$("#prev").click(function () {
  moveToSelected("prev");
});

$("#next").click(function () {
  moveToSelected("next");
});

$(".thumbnail-btn").click(function () {
  var targetIndex = $(this).data("target");
  var targetDiv = $("#carousel div").eq(targetIndex);
  moveToSelected(targetDiv);
});
