/**
 * 게시글 내용을 AI 교정 API에 전달합니다.
 * Fetch API를 이용해 비동기 POST 요청을 전송합니다.
 * 서버에서 반환한 교정 결과를 화면에 출력합니다.
 */
async function proofread() {
    const contentInput = document.querySelector("#content");
    const result = document.querySelector("#proofreadResult");
    const button = document.querySelector("#proofreadButton");
    if (!contentInput || !result) return;

    const content = contentInput.value;
    if (!content.trim()) {
        result.textContent = "교정할 내용을 먼저 입력해주세요.";
        return;
    }

    // 교정 요청 시작(중복 클릭 방지)
    result.textContent = "AI 교정 중...";
    if (button) button.disabled = true;

    try {
        const response = await fetch("/api/agent/proofread", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            },
            body: new URLSearchParams({content})
        });

        // 서버가 보낸 안내 문구(또는 교정 결과)를 그대로 읽는다.
        const text = await response.text();

        // HTTP 응답 상태 확인
        if (!response.ok) {
            if (response.status === 401) {
                alert("로그인이 필요합니다.");
                location.href = "/login";
                return;
            }
            result.textContent = text || "AI 교정 중 오류가 발생했습니다.";
            return;
        }

        // 교정 결과 출력
        result.textContent = text;

    } catch (error) {
        console.error("AI 교정 실패:", error);
        result.textContent = "AI 교정 중 오류가 발생했습니다.";
    } finally {
        if (button) button.disabled = false;
    }
}
