import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

import router from './router'
import { createPinia } from 'pinia'

import App from './App.vue'

const app = createApp(App)

// 注册 Element Plus 的所有图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}

// 注册路由和状态管理
app.use(router)
app.use(createPinia())

// 引入 Element Plus 并注册到全局
app.use(ElementPlus)

app.mount('#app')