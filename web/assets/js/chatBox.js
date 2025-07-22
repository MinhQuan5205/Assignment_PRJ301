/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

class ChatBoxAPI {
    constructor(apiKey, botId, baseUrl = 'https://ai.ftes.vn') {
        this.apiKey = apiKey;
        this.botId = botId;
        this.baseUrl = baseUrl;
        this.conversationId = null;
        this.modelName = "gemini-2.5-flash-preview-05-20";
    }

    async sendMessage(query, conversationId = null, attachments = []) {
        const payload = {
            query: query,
            bot_id: this.botId,
            conversation_id: conversationId || this.conversationId,
            model_name: this.modelName,
            api_key: this.apiKey,
            attachs: attachments
        };

        console.log('Sending payload:', payload); // Debug log
        console.log('API Endpoint:', this.baseUrl + '/api/ai/rag_agent_template/stream'); // Debug log

        try {
            const controller = new AbortController();
            const timeoutId = setTimeout(() => controller.abort(), 30000);

            const response = await fetch(this.baseUrl + '/api/ai/rag_agent_template/stream', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Accept': 'application/json',
                    'Origin': window.location.origin,
                },
                body: JSON.stringify(payload),
                signal: controller.signal
            });

            clearTimeout(timeoutId);

            console.log('Response status:', response.status);
            console.log('Response headers:', [...response.headers.entries()]);

            if (!response.ok) {
                const errorText = await response.text();
                console.error('Error response:', errorText);
                throw new Error(`HTTP ${response.status}: ${errorText || response.statusText}`);
            }

            // Xử lý streaming response nếu có
            const contentType = response.headers.get('content-type');

            if (contentType && contentType.includes('text/stream')) {
                // Xử lý stream response
                const reader = response.body.getReader();
                const decoder = new TextDecoder();
                let result = '';

                while (true) {
                    const {done, value} = await reader.read();
                    if (done)
                        break;

                    const chunk = decoder.decode(value, {stream: true});
                    result += chunk;

                    // Có thể xử lý real-time streaming ở đây
                    console.log('Stream chunk:', chunk);
                }

                const data = {message: result, response: result};

                if (data.conversation_id) {
                    this.conversationId = data.conversation_id;
                }

                return data;
            } else {
                // Xử lý JSON response thông thường
                const data = await response.json();
                console.log('Response data:', data);

                if (data.conversation_id) {
                    this.conversationId = data.conversation_id;
                }

                return data;
            }

        } catch (error) {
            console.error('Detailed error:', error);

            if (error.name === 'AbortError') {
                throw new Error('Kết nối quá chậm. Vui lòng kiểm tra mạng và thử lại.');
            } else if (error.message.includes('Failed to fetch') || error.message.includes('NetworkError')) {
                throw new Error('Không thể kết nối tới https://ai.ftes.vn. Kiểm tra kết nối mạng.');
            } else if (error.message.includes('CORS')) {
                throw new Error('Lỗi CORS. Server cần cấu hình cho phép cross-origin requests.');
            } else {
                throw new Error(error.message || 'Lỗi không xác định');
            }
    }
    }

    // Test kết nối tới endpoint thực tế
    async testConnection() {
        try {
            const response = await fetch(this.baseUrl + '/api/ai/rag_agent_template/stream', {
                method: 'OPTIONS', // Preflight request
                headers: {
                    'Accept': 'application/json',
                    'Origin': window.location.origin,
                }
            });
            return response.ok || response.status === 404; // 404 cũng ok vì endpoint tồn tại
        } catch (error) {
            console.error('Connection test failed:', error);
            return false;
        }
    }
}

// Khởi tạo ChatBot API với endpoint thực tế
const chatBot = new ChatBoxAPI(
        "AIzaSyCYXCDDrjSObsI_2BmPY1y2Dm3pYQ2kum0", // API key của bạn
        "8970f6b7a1b58fa4d37d0c79", // Bot ID của bạn
        "https://ai.ftes.vn" // Endpoint chính xác của bạn
        );

// Các phần tử DOM
const chatContent = document.getElementById('chat-content');
const chatForm = document.getElementById('chat-form');
const chatInput = document.getElementById('chat-input');
const chatbox = document.getElementById('chatbox');
const chatToggle = document.getElementById('chat-toggle');

// Hàm toggle chat
function toggleChat(show) {
    if (show) {
        chatbox.style.display = 'flex';
        chatToggle.style.display = 'none';
        chatInput.focus();

        // Thêm tin nhắn chào mừng nếu chưa có tin nhắn nào
        if (chatContent.children.length === 0) {
            addMessage('Xin chào! Tôi là BookBot. Bạn có thể hỏi tôi về sách, tác giả, hoặc bất kỳ điều gì liên quan đến văn học! 📚', 'bot');
        }
    } else {
        chatbox.style.display = 'none';
        chatToggle.style.display = 'block';
    }
}

// Hàm thêm tin nhắn vào chat
function addMessage(message, sender) {
    const messageDiv = document.createElement('div');
    messageDiv.className = 'message ' + (sender === 'user' ? 'user-message' : 'bot-message');
    messageDiv.textContent = message;
    chatContent.appendChild(messageDiv);
    chatContent.scrollTop = chatContent.scrollHeight;
}

// Hàm hiển thị typing indicator
function showTyping() {
    const typingDiv = document.createElement('div');
    typingDiv.className = 'message typing-indicator';
    typingDiv.id = 'typing-indicator';
    typingDiv.textContent = 'BookBot đang trả lời...';
    chatContent.appendChild(typingDiv);
    chatContent.scrollTop = chatContent.scrollHeight;
}

// Hàm ẩn typing indicator
function hideTyping() {
    const typingIndicator = document.getElementById('typing-indicator');
    if (typingIndicator) {
        typingIndicator.remove();
    }
}

// Hàm hiển thị lỗi
function showError(errorMessage) {
    const errorDiv = document.createElement('div');
    errorDiv.className = 'message error-message';
    errorDiv.textContent = 'Lỗi: ' + errorMessage;
    chatContent.appendChild(errorDiv);
    chatContent.scrollTop = chatContent.scrollHeight;
}

// Xử lý submit form
chatForm.addEventListener('submit', async (e) => {
    e.preventDefault();

    const message = chatInput.value.trim();
    if (!message)
        return;

    // Thêm tin nhắn của user
    addMessage(message, 'user');
    chatInput.value = '';

    // Hiển thị typing indicator
    showTyping();

    try {
        // Gửi tin nhắn đến API
        const response = await chatBot.sendMessage(message);

        // Ẩn typing indicator
        hideTyping();

        // Hiển thị phản hồi từ bot
        // Tùy vào cấu trúc response từ API, bạn có thể cần điều chỉnh
        const botMessage = response.message || response.response || response.answer || JSON.stringify(response);
        addMessage(botMessage, 'bot');

    } catch (error) {
        hideTyping();

        // Hiển thị lỗi chi tiết hơn
        let errorMessage = 'Đã xảy ra lỗi: ' + error.message;

        // Gợi ý giải pháp dựa trên loại lỗi
        if (error.message.includes('https://ai.ftes.vn')) {
            errorMessage += '\n💡 Kiểm tra kết nối tới ai.ftes.vn hoặc liên hệ admin server.';
        } else if (error.message.includes('mạng')) {
            errorMessage += '\n💡 Kiểm tra kết nối internet của bạn.';
        } else if (error.message.includes('CORS')) {
            errorMessage += '\n💡 Liên hệ admin để cấu hình CORS trên server.';
        }

        showError(errorMessage);
        console.error('Chat error:', error);
    }
});

// Xử lý phím Enter trong input
chatInput.addEventListener('keypress', (e) => {
    if (e.key === 'Enter' && !e.shiftKey) {
        e.preventDefault();
        chatForm.dispatchEvent(new Event('submit'));
    }
});

// Khởi tạo trạng thái ban đầu
document.addEventListener('DOMContentLoaded', function () {
    // Chat được ẩn ban đầu
    chatbox.style.display = 'none';
    chatToggle.style.display = 'block';
});

// Hàm để test kết nối API
async function testAPI() {
    console.log('Testing API connection...');
    addMessage('Đang kiểm tra kết nối API...', 'bot');

    try {
        // Test kết nối cơ bản
        const isConnected = await chatBot.testConnection();

        if (isConnected) {
            addMessage('✅ Kết nối API thành công!', 'bot');
        } else {
            addMessage('❌ Không thể kết nối API. Kiểm tra URL và cấu hình server.', 'bot');
        }

        // Test gửi tin nhắn
        showTyping();
        const response = await chatBot.sendMessage('Hello, this is a test message');
        hideTyping();

        const botMessage = response.message || response.response || response.answer || 'API hoạt động bình thường!';
        addMessage('📨 ' + botMessage, 'bot');

    } catch (error) {
        hideTyping();
        addMessage('❌ Test API thất bại: ' + error.message, 'bot');

        // Gợi ý debug
        addMessage('🔧 Debug info: Mở Developer Tools (F12) để xem log chi tiết', 'bot');
    }
}

// Thêm nút test API (tạm thời)
function addTestButton() {
    const testBtn = document.createElement('button');
    testBtn.textContent = '🧪 Test API';
    testBtn.onclick = testAPI;
    testBtn.style.cssText = 'position:fixed; bottom:80px; right:20px; z-index:9999; padding:8px 12px; background:#28a745; color:white; border:none; border-radius:4px; cursor:pointer;';
    document.body.appendChild(testBtn);
}

// Uncomment để thêm nút test API
// addTestButton();

// Uncomment dòng dưới để test API khi trang load
// setTimeout(testAPI, 2000);

//////        ChatBox 
//////        <div id="chatbox" style="position: fixed; bottom: 20px; right: 20px; width: 300px; max-height: 500px; background: white; border: 1px solid #ccc; border-radius: 8px; display: flex; flex-direction: column; z-index: 9999; box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);">
//////            <div style="background: #007BFF; color: white; padding: 10px; font-weight: bold; border-radius: 8px 8px 0 0;">
////                Chat với BookBot 📚
////                <button onclick="toggleChat(false)" style="float:right; background:none; border:none; color:white; font-size:16px; cursor:pointer;">×</button>
////            </div>//
//////            <div id="chat-content" style="padding: 10px; flex: 1; overflow-y: auto; font-size: 14px; min-height: 200px; max-height: 350px;"></div>
//////            <form id="chat-form" style="display: flex; border-top: 1px solid #ccc;">
////                <input type="text" id="chat-input" placeholder="Hỏi về sách..." style="flex: 1; padding: 8px; border: none; outline: none;" autocomplete="off">
//                <button type="submit" style="padding: 8px 12px; background: #007BFF; color: white; border: none; cursor: pointer;">Gửi</button>
//            </form>//
////        </div>//
//
//         Nút bật chat 
//        <button id="chat-toggle" onclick="toggleChat(true)"
//                style="position: fixed; bottom: 20px; right: 20px; z-index: 9998; padding: 15px; background-color: #007BFF; color: white; border: none; border-radius: 50%; font-size: 20px; cursor: pointer; box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);">
//            💬
//        </button>

