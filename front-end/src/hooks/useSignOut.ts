import axios from 'axios'
import config from '@/constants/config'
import { useContext } from 'react'
import AuthContext from '@/context/AuthContext'

export default function useSignOut() {
  const context = useContext(AuthContext)

  if (!context) {
    throw new Error('useSignOut must be used within an AuthProvider')
  }

  const { deleteCurrentUser, setIsLoggedOut, auth } = context

  console.log('auth', auth);

  const signOut = async () => {
    deleteCurrentUser()
    setIsLoggedOut(true)
    const res = await axios.post(
      `${config.baseUrl}/users/sign-out`,
      {},
      {
        withCredentials: true,
        headers: {
          Authorization: `${auth?.accessToken}`
        }
      }
    )
    return res.data
  }

  return signOut
}
