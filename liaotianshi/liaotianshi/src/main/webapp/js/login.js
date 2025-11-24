/**
 * 登录页面功能 - 切换登录方式
 */
document.addEventListener('DOMContentLoaded', function() {
    const normalLoginBtn = document.getElementById('normalLoginBtn');
    const phoneLoginBtn = document.getElementById('phoneLoginBtn');
    const normalLoginForm = document.getElementById('normalLoginForm');
    const phoneLoginForm = document.getElementById('phoneLoginForm');

    // 普通账号登录按钮点击事件
    normalLoginBtn.addEventListener('click', function() {
        switchToNormalLogin();
    });

    // 手机账号登录按钮点击事件
    phoneLoginBtn.addEventListener('click', function() {
        switchToPhoneLogin();
    });

    /**
     * 切换到普通账号登录
     */
    function switchToNormalLogin() {
        normalLoginBtn.classList.add('active');
        phoneLoginBtn.classList.remove('active');

        normalLoginForm.classList.add('active');
        phoneLoginForm.classList.remove('active');

        // 清空手机登录表单
        document.getElementById('phoneNumber').value = '';
        document.getElementById('phonePassword').value = '';
    }

    /**
     * 切换到手机账号登录
     */
    function switchToPhoneLogin() {
        phoneLoginBtn.classList.add('active');
        normalLoginBtn.classList.remove('active');

        phoneLoginForm.classList.add('active');
        normalLoginForm.classList.remove('active');

        // 清空普通登录表单
        document.getElementById('username').value = '';
        document.getElementById('password').value = '';
    }

    // 表单提交验证
    normalLoginForm.addEventListener('submit', function(e) {
        if (!validateNormalLogin()) {
            e.preventDefault();
        }
    });

    phoneLoginForm.addEventListener('submit', function(e) {
        if (!validatePhoneLogin()) {
            e.preventDefault();
        }
    });

    /**
     * 验证普通登录表单
     */
    function validateNormalLogin() {
        const username = document.getElementById('username').value.trim();
        const password = document.getElementById('password').value.trim();

        if (!username) {
            alert('请输入用户名');
            document.getElementById('username').focus();
            return false;
        }

        if (!password) {
            alert('请输入密码');
            document.getElementById('password').focus();
            return false;
        }

        return true;
    }

    /**
     * 验证手机登录表单
     */
    function validatePhoneLogin() {
        const phoneNumber = document.getElementById('phoneNumber').value.trim();
        const password = document.getElementById('phonePassword').value.trim();

        if (!phoneNumber) {
            alert('请输入手机号');
            document.getElementById('phoneNumber').focus();
            return false;
        }

        if (!/^1[3-9]\d{9}$/.test(phoneNumber)) {
            alert('请输入正确的手机号');
            document.getElementById('phoneNumber').focus();
            return false;
        }

        if (!password) {
            alert('请输入密码');
            document.getElementById('phonePassword').focus();
            return false;
        }

        return true;
    }
});