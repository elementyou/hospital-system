import request from '@/utils/request'

// 1. 获取候诊简易队列（传入医生ID和日期）
export function getWaitingList(data) {
    return request({
        url: '/doctor/waiting-list',
        method: 'get',
        params: data
    })
}

// 2. 叫号，分配排队序号
export function callNextPatient(appointmentId) {
    return request({
        url: '/doctor/call-number',
        method: 'put',
        data: { appointmentId }
    })
}

// 3. 开始接诊
export function startConsult(appointmentId) {
    return request({
        url: '/doctor/consult',
        method: 'put',
        data: { appointmentId }
    })
}

// 4. 获取患者详情（传入预约ID）
export function getPatientDetail(appointmentId) {
    return request({
        url: '/doctor/patient-detail',
        method: 'get',
        params: { appointmentId }
    })
}

// 5. 提交保存病历
export function submitMedicalRecord(data) {
    return request({
        url: '/medical-record',
        method: 'post',
        data: data
    })
}

// 新加这个，调完整字段的接口
export function getQueueList(data) {
    return request({
        url: '/doctor/queue-list',
        method: 'get',
        params: data
    })
}