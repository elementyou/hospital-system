import { createRouter, createWebHistory } from 'vue-router'
import Layout from '../layout/Index.vue'

const router = createRouter({
    history: createWebHistory(),
    routes: [
        // 访问根路径 "/" 时，自动跳转到登录页
        {
            path: '/',
            redirect: '/login'
        },
        // 登录页
        {
            path: '/login',
            name: 'Login',
            component: () => import('../views/Login.vue')
        },
        // 系统主框架（嵌套路由）
        {
            path: '/home',
            component: Layout,
            redirect: '/home/departments', // 默认跳转科室列表页（你可以改成 appointments）
            children: [
                {
                    path: 'index',
                    name: 'PatientIndex',
                    component: () => import('../views/home/Home.vue')
                },
                {
                    path: 'appointments',
                    name: 'Appointments',
                    component: () => import('../views/home/Appointments.vue')
                },
                {
                    path: 'prescriptions',
                    name: 'Prescriptions',
                    component: () => import('../views/home/Prescriptions.vue')
                },
                {
                    path: 'departments',
                    name: 'Departments',
                    component: () => import('@/views/home/DoctorsAndSchedules.vue')
                },
                {
                    path: 'my-appointments',
                    name: 'MyAppointments',
                    component: () => import('@/views/home/MyAppointments.vue'),
                    meta: { title: '我的预约' }
                }
            ]
        },
        //医生工作模块父级
        {
            path: '/doctor',
            component: Layout,
            redirect: '/doctor/workstation', // 默认跳转到工作台
            meta: { title: '医生工作站' },
            children: [
                {
                    path: 'workstation',
                    name: 'DoctorWorkstation',
                    component: () => import('../views/doctor/Workstation.vue'), // 注意这里的引用路径
                    meta: { title: '接诊工作台' }
                },
                {
                    path: '/doctor/queue-list',
                    name: 'DoctorQueueList',
                    component: () => import('@/views/doctor/QueueList.vue')
                }
            ]
        },
        //管理员模块
        {
            path: '/admin',
            component: Layout,
            redirect: '/admin/dashboard',
            children: [
                {
                    path: 'dashboard',
                    name: 'AdminDashboard',
                    component: () => import('@/views/admin/Dashboard.vue')
                },

                //先注释掉User.vue界面
                /*
                {
                    path: 'users',
                    name: 'AdminUsers',
                    component: () => import('@/views/admin/User.vue')
                }
                */
                {
                    path: 'user',
                    name: 'UserManage',
                    component: () => import('../views/admin/UserManage.vue') },
                {
                    path: 'deptManage',
                    name: 'DeptManage',
                    component: () => import('@/views/admin/DeptManage.vue')
                },
                {
                    path: 'medicineManage',
                    name: 'MedicineManage',
                    component: () => import('@/views/admin/MedicineManage.vue')
                },
                {
                    path: '/admin/doctorManage',
                    name: 'DoctorManage',
                    component: () => import('../views/admin/DoctorManage.vue')
                },
                {
                    path: '/admin/scheduleManage',
                    name: 'ScheduleManage',
                    component: () => import('../views/admin/ScheduleManage.vue')
                }


            ]
        }
    ]
})

export default router