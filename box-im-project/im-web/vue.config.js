const path = require('path')
const fs = require('fs')

module.exports = {
	devServer: {
		proxy: {
			'/api': {
				target: 'http://127.0.0.1:8888',
				changeOrigin: true,
				ws: false,
				pathRewrite: {
					'^/api': ''
				}
			}
		}
	},
	// 进一步加速开发构建
	css: {
		// 关闭 CSS SourceMap，减少首次与热更新的构建时间
		sourceMap: false
	},
	// 关闭保存时的 ESLint 检查，避免在 devServer 中增加构建开销
	lintOnSave: false
}