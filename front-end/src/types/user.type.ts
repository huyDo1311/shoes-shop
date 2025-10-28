import { z } from 'zod'

export const userSchema = z.object({
 email: z.string().email(),
  userName: z.string().optional(),
  avatar: z.string().optional(),
  dateOfBirth: z.string().optional(), // sửa lại
  phone: z
    .string()
    .regex(/^0\d{9}$/, "Invalid phone number. Must start with 0 and have 10 digits.")
    .optional(),
  address: z.string().optional(),
  isActive: z.boolean().optional(),
  password: z.string().optional(),
  confirmPassword: z.string().optional(),
  roles: z.array(z.string()).optional() 
})

export type UserFormType = z.infer<typeof userSchema>

type Role = 'User' | 'Admin'

export interface User {
  _id: string
  roles: Role[]
  email: string
  name?: string
  date_of_birth?: string // ISO 8610
  avatar?: string
  address?: string
  phone?: string
  createdAt: string
  updatedAt: string
}
