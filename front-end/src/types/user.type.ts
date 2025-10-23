import { z } from "zod";

export const userSchema = z.object({
  email: z.string().email(),
  password: z.string().optional(),
  confirmPassword: z.string().optional(),
});

export type UserFormType = z.infer<typeof userSchema> ;

export const addressSchema = z.object({
  fullName: z.string(),
  phoneNumber: z.string(),
  district: z.string(),
  province: z.string(),
  ward: z.string(),
  specify: z.string(),
});

export type AddressType = z.infer<typeof addressSchema>;

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