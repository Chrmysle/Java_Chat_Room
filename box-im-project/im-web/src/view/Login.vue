<template>
	<div class="login-view">
		<div class="login-content">
			<div class="auth-decor">
				<img src="/auth-decor.svg" alt="decor" />
			</div>
			<div class="login-card">
				<form class="login-form" @submit.prevent="submitForm()">
					<div class="login-brand">
						<img class="logo" src="/logo.svg" />
						<div>登录讯捷聊天平台</div>
					</div>

					<div class="form-item" v-show="false">
						<label>终端</label>
						<input type="text" v-model="loginForm.terminal" autocomplete="off" />
					</div>

					<div class="form-item">
						<label>用户名</label>
						<input type="text" v-model="loginForm.userName" autocomplete="off" placeholder="用户名" />
					</div>

					<div class="form-item">
						<label>密码</label>
						<input type="password" v-model="loginForm.password" autocomplete="off" placeholder="密码" />
					</div>

					<div class="form-actions">
						<button type="submit" class="btn primary">登 录</button>
						<button type="button" class="btn" @click="resetForm()">清 空</button>
					</div>

					<div class="register">
						<a class="link" href="#" @click.prevent="$router.push('/register')">没有账号，前往注册</a>
					</div>
				</form>
			</div>
		</div>
		<icp></icp>
	</div>
</template>

<script>
import Icp from '../components/common/Icp.vue'
export default {
	name: "login",
	components: { Icp },
	data() {
		return {
			loginForm: {
				terminal: this.$enums.TERMINAL_TYPE.WEB,
				userName: '',
				password: ''
			}
		};
	},
	methods: {
		submitForm() {
			if (!this.loginForm.userName) {
				this.$message.error('请输入用户名');
				return;
			}
			if (!this.loginForm.password) {
				this.$message.error('请输入密码');
				return;
			}
			this.$http({
				url: "/login",
				method: 'post',
				data: this.loginForm
			})
				.then((data) => {
					// 保存密码到cookie(不安全)
					this.setCookie('username', this.loginForm.userName);
					this.setCookie('password', this.loginForm.password);
					// 保存token
					sessionStorage.setItem("accessToken", data.accessToken);
					sessionStorage.setItem("refreshToken", data.refreshToken);
					this.$message.success("登录成功");
					this.$router.push("/home/chat");
				})
		},
		resetForm() {
			this.loginForm.userName = '';
			this.loginForm.password = '';
		},
		getCookie(name) {
			let reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");
			let arr = document.cookie.match(reg)
			if (arr) {
				return unescape(arr[2]);
			}
			return '';
		},
		setCookie(name, value) {
			document.cookie = name + "=" + escape(value);
		}

	},
	mounted() {
		this.loginForm.userName = this.getCookie("username");
		// cookie存密码并不安全，暂时是为了方便
		this.loginForm.password = this.getCookie("password");
	}
}
</script>

<style scoped lang="scss">
 .login-view {
 	width: 100%;
 	height: 100%;
 	background: #f5f7fa;
 	background-size: cover;
 	box-sizing: border-box;
 	position: relative;
 	font-family: var(--im-font-family);
 
 	.login-content {
 		position: relative;
 		display: flex;
 		justify-content: center;
 		align-items: center;
 		min-height: 100vh;
 		padding: 24px 16px;
 
 		.login-card {
 			width: 420px;
 			background: #fff;
 			border: 1px solid #EBEEF5;
 			border-radius: 4px;
 			box-shadow: var(--im-box-shadow-base);
 		}
 
 		.login-form {
 			height: auto;
 			padding: 24px;
 			background: transparent;
 			box-shadow: none;
 			border: none;
 
 			.login-brand {
 				display: flex;
 				justify-content: center;
 				align-items: center;
 				line-height: 50px;
 				margin: 6px 0 16px 0;
 				padding-bottom: 12px;
 				border-bottom: 1px solid #EBEEF5;
 				font-size: 20px;
 				font-weight: 700;
 				letter-spacing: 1px;
 				text-transform: none;
 				text-align: center;
 				color: #303133;
 
 				.logo { width: 36px; height: 36px; margin-right: 10px; }
 			}
 
 			.form-item { margin-bottom: 18px; display: flex; flex-direction: column; }
 			.form-item label { margin-bottom: 6px; color: #606266; font-size: 14px; }
 			.form-item input {
 				height: 40px; border-radius: 4px; border: 1px solid #dcdfe6; padding: 0 12px; outline: none; font-size: 14px; color: #606266;
 				transition: border-color .2s ease, box-shadow .2s ease;
 				background-color: #fff;
 			}
 			.form-item input::placeholder { color: #C0C4CC; }
 			.form-item input:hover { border-color: #c0c4cc; }
 			.form-item input:focus { border-color: var(--im-color-primary); box-shadow: 0 0 0 2px rgba(64, 158, 255, .12); }
 			.form-item.is-error input { border-color: var(--im-color-danger); }
 			.form-item .help { margin-top: 6px; font-size: 12px; color: var(--im-color-danger); }
 
 			.form-actions { display: flex; gap: 8px; align-items: center; margin-top: 4px; }
 			.btn {
 				height: 36px; padding: 0 16px; border: 1px solid #dcdfe6; background: #fff; color: #606266; border-radius: 4px; cursor: pointer; user-select: none;
 				transition: color .2s ease, background-color .2s ease, border-color .2s ease, box-shadow .2s ease;
 			}
 			.btn:hover { color: var(--im-color-primary); border-color: var(--im-color-primary); background: var(--im-color-primary-light-9); }
 			.btn:active { box-shadow: inset 0 1px 2px rgba(0,0,0,.08); }
 			.btn:disabled { cursor: not-allowed; color: #C0C4CC; background: #f5f7fa; border-color: #e4e7ed; }
 
 			.btn.primary { background: var(--im-color-primary); border-color: var(--im-color-primary); color: #fff; }
 			.btn.primary:hover { background: var(--im-color-primary-light-1); border-color: var(--im-color-primary-light-1); }
 			.btn.primary:active { filter: saturate(0.95); }
 
 			.register { display: flex; justify-content: flex-end; line-height: 40px; margin-top: 6px; }
 			.link { color: var(--im-color-primary); text-decoration: none; }
 			.link:hover { text-decoration: underline; }
 		}
 	}
 }
 
 /* 响应式优化 */
 @media (max-width: 480px) {
 	.login-view .login-content .login-card { width: 92vw; }
 	.login-view .login-content { padding: 24px 12px; }
 }
 
 .auth-decor { display: none; }
 .auth-decor img { width: 420px; height: auto; border-radius: 6px; box-shadow: var(--im-box-shadow-light); }
 
 @media (min-width: 768px) {
 	.auth-decor { display: block; }
 }
</style>