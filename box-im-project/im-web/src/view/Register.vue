<template>
	<div class="register-view">
		<div class="auth-decor">
			<img src="/auth-decor.svg" alt="decor" />
		</div>
		<div>
			<div class="register-card">
				<form class="web-ruleForm" @submit.prevent="submitForm()">
					<div class="register-brand">
						<img class="logo" src="/logo.svg" />
						<div>欢迎成为讯捷聊天平台的用户</div>
					</div>

					<div class="form-item">
						<label>用户名</label>
						<input type="text" v-model="registerForm.userName" autocomplete="off" placeholder="用户名(登录使用)" />
					</div>

					<div class="form-item">
						<label>昵称</label>
						<input type="text" v-model="registerForm.nickName" autocomplete="off" placeholder="昵称" />
					</div>

					<div class="form-item">
						<label>密码</label>
						<input type="password" v-model="registerForm.password" autocomplete="off" placeholder="密码" />
					</div>

					<div class="form-item">
						<label>确认密码</label>
						<input type="password" v-model="registerForm.confirmPassword" autocomplete="off" placeholder="确认密码" />
					</div>

					<div class="form-actions">
						<button type="submit" class="btn primary">注 册</button>
						<button type="button" class="btn" @click="resetForm()">清 空</button>
					</div>

					<div class="to-login">
						<a class="link" href="#" @click.prevent="$router.push('/login')">已有账号，前往登录</a>
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
		const validate = {
			userName: v => !!v || '请输入用户名',
			nickName: v => !!v || '请输入昵称',
			password: v => !!v || '请输入密码',
			confirmPassword: (v, form) => (v && v === form.password) || '两次密码输入不一致'
		};
		return {
			registerForm: {
				userName: '',
				nickName: '',
				password: '',
				confirmPassword: ''
			},
			validate
		};
	},
	methods: {
		submitForm() {
			const f = this.registerForm;
			if (typeof this.validate.userName(f.userName) !== 'boolean') return this.$message.error(this.validate.userName(f.userName));
			if (typeof this.validate.nickName(f.nickName) !== 'boolean') return this.$message.error(this.validate.nickName(f.nickName));
			if (typeof this.validate.password(f.password) !== 'boolean') return this.$message.error(this.validate.password(f.password));
			const cp = this.validate.confirmPassword(f.confirmPassword, f);
			if (typeof cp !== 'boolean') return this.$message.error(cp);

			this.$http({ url: "/register", method: 'post', data: this.registerForm })
				.then(() => { this.$message.success("注册成功!"); })
		},
		resetForm() {
			this.registerForm = { userName: '', nickName: '', password: '', confirmPassword: '' };
		}
	}
}
</script>

<style scoped lang="scss">
.register-view {
    position: fixed;
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100%;
    height: 100%;
    background: #f5f7fa;
    font-family: var(--im-font-family);

    .register-card {
        width: 520px;
        background: #fff;
        border: 1px solid #EBEEF5;
        border-radius: 4px;
        box-shadow: var(--im-box-shadow-base);
    }

    .web-ruleForm {
        height: auto;
        padding: 24px;
        background: transparent;
        box-shadow: none;
        border: none;

        .register-brand {
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
            text-align: center;
            text-transform: none;
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

        .to-login { display: flex; justify-content: flex-end; line-height: 40px; margin-top: 6px; }
        .link { color: var(--im-color-primary); text-decoration: none; }
        .link:hover { text-decoration: underline; }
    }
}

/* 响应式优化 */
@media (max-width: 480px) {
    .register-view .register-card { width: 92vw; }
}

.auth-decor { display: none; }
.auth-decor img { width: 520px; height: auto; border-radius: 6px; box-shadow: var(--im-box-shadow-light); }

@media (min-width: 768px) {
    .register-view { grid-template-columns: 520px 520px; justify-content: center; align-items: center; }
    .register-view > .auth-decor { order: -1; }
    .register-view .auth-decor { display: block; }
}
</style>
