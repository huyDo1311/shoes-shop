
import type { UserFormType } from '@/types/user.type'
import type { SuccessResponse } from '@/types/utils.type'
import http from '@/utils/http'


export const URL_USER = 'users'

const userApi = {
  getUserInfo: (body: { email: string}) => http.post<SuccessResponse<UserFormType>>(`${URL_USER}/info`, body),
  updateUserInfo: (body: UserFormType) => http.put<SuccessResponse<UserFormType>>(`${URL_USER}/update`, body)
}

export default userApi
