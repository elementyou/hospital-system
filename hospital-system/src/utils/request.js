import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '@/router'

const request = axios.create({
    baseURL: '/api',
    timeout: 10000
})

request.interceptors.request.use(config => {
    const token = localStorage.getItem('token')
    if (token) {
        config.headers.Authorization = `Bearer ${token}`
    }
    return config
})

request.interceptors.response.use(
    response => {
        const res = response.data

        // 后端返回 200 代表成功
        if (res.code !== 200) {
            ElMessage.error(res.message || '请求失败')
            return Promise.reject(new Error(res.message || '请求失败'))
        }

        // 返回 data 字段里的数据
        return res.data
    },
    error => {
        if (error.response) {
            const status = error.response.status
            if (status === 401) {
                ElMessage.error('登录已过期，请重新登录')
                localStorage.clear()
                router.push('/login')
            } else {
                ElMessage.error(error.response.data?.message || '请求失败')
            }
        } else {
            ElMessage.error('网络异常，请检查后端是否启动')
        }
        return Promise.reject(error)
    }
)

export default request