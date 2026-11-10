import request from '@/utils/request'

// 提交预约
export function addAppointment(data) {
    return request.post('/appointment', data)
}

// 1. 查询科室列表
export function getDepartmentList() {
    return request({
        url: '/appointment/departments',
        method: 'get'
    })
}

export function getDoctorList(params) {
    return request({
        url: '/appointment/doctors',
        method: 'GET',
        params
    })
}

export function getScheduleList(params) {
    return request({
        url: '/appointment/schedules',
        method: 'GET',
        params
    })
}

// 获取我的预约记录
export function getMyAppointmentList(query) {
    return request({
        url: '/appointment/my-list',
        method: 'get',
        params: query
    })
}
