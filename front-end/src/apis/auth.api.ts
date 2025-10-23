
import type { AuthResponse } from '@/types/auth.type'
import http from '@/utils/http'


export const URL_SIGNIN = 'users/sign-in'
export const URL_SIGNUP = 'users/sign-up'
export const URL_SIGNOUT = 'users/sign-out'
export const URL_REFRESH_TOKEN = 'users/refresh-token'

const authApi = {
  signup(body: { email: string; password: string }) {
    return http.post<AuthResponse>(URL_SIGNUP, body)
  },
  signin(body: { email: string; password: string }) {
    return http.post<AuthResponse>(URL_SIGNIN, body)
  },
  signout() {
    return http.post(URL_SIGNOUT)
  }
}

export default authApi
