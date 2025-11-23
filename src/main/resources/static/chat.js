let username = prompt("请输入你的用户名：");
if (!username) username = "游客" + Math.floor(Math.random() * 1000);

const ws = new WebSocket(`ws://172.20.10.3:8080/chat?username=${username}`);

const chatBox = document.getElementById("chatBox");
const messageInput = document.getElementById("messageInput");
const sendBtn = document.getElementById("sendBtn");
const userSelect = document.getElementById("userSelect");

let onlineUsers = new Set();

// 发送消息函数
function sendMessage() {
    const content = messageInput.value.trim();
    if (!content) return;
    const toUser = userSelect.value || null;
    const type = toUser ? "private" : "group";

    const msg = { fromUser: username, toUser, content, type };
    ws.send(JSON.stringify(msg));

    // 自己发送的消息也显示在聊天框
    displayMessage({ ...msg, time: new Date().toLocaleTimeString() });

    messageInput.value = "";
}

// 点击按钮发送
sendBtn.addEventListener("click", sendMessage);

// 回车发送
messageInput.addEventListener("keypress", (e) => {
    if (e.key === "Enter") {
        sendMessage();
    }
});

// 接收消息
ws.onmessage = (event) => {
    const msg = JSON.parse(event.data);

    // 群聊消息显示，私聊显示给自己或自己发出的
    if (msg.type === "group" || msg.toUser === username || msg.fromUser === username) {
        displayMessage(msg);
    }

    // 更新在线用户列表（排除系统消息和自己）
    if (msg.fromUser && msg.fromUser !== "系统") {
        onlineUsers.add(msg.fromUser);
        updateUserSelect();
    }
};

// 显示消息
function displayMessage(msg) {
    const div = document.createElement("div");
    div.classList.add("message");
    div.classList.add(msg.type); // group 或 private
    const toText = msg.type === "private" ? `(私聊 → ${msg.toUser})` : "";
    div.textContent = `[${msg.time || new Date().toLocaleTimeString()}] ${msg.fromUser} ${toText}: ${msg.content}`;
    chatBox.appendChild(div);
    chatBox.scrollTop = chatBox.scrollHeight;
}

// 更新用户选择列表
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
