let username = prompt("请输入你的用户名：");
if (!username) username = "游客" + Math.floor(Math.random() * 1000);

const ws = new WebSocket(`ws://172.20.10.3:8080/chat?username=${username}`);

const chatBox = document.getElementById("chatBox");
const messageInput = document.getElementById("messageInput");
const sendBtn = document.getElementById("sendBtn");
const userSelect = document.getElementById("userSelect");

let onlineUsers = new Set();

sendBtn.addEventListener("click", () => {
    const content = messageInput.value.trim();
    if (!content) return;
    const toUser = userSelect.value || null;
    const type = toUser ? "private" : "group";

    const msg = { fromUser: username, toUser, content, type };
    ws.send(JSON.stringify(msg));
    messageInput.value = "";
});

ws.onmessage = (event) => {
    const msg = JSON.parse(event.data);
    displayMessage(msg);

    if (msg.fromUser && msg.fromUser !== "系统") {
        onlineUsers.add(msg.fromUser);
        updateUserSelect();
    }
};

function displayMessage(msg) {
    const div = document.createElement("div");
    div.classList.add("message");
    const toText = msg.type === "private" ? `(私聊 → ${msg.toUser})` : "";
    div.textContent = `[${msg.time}] ${msg.fromUser} ${toText}: ${msg.content}`;
    chatBox.appendChild(div);
    chatBox.scrollTop = chatBox.scrollHeight;
}

function updateUserSelect() {
    userSelect.innerHTML = '<option value="">所有人（群聊）</option>';
    onlineUsers.forEach(user => {
        if (user !== username) {
            const opt = document.createElement("option");
            opt.value = user;
            opt.textContent = user;
            userSelect.appendChild(opt);
        }
    });
}

ws.onopen = () => console.log("WebSocket 已连接");
ws.onclose = () => alert("服务器已关闭连接");
ws.onerror = () => console.error("WebSocket 错误");
