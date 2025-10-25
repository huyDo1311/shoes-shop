import type { Variant } from '@/types/variant.type'
import type { NumberMap } from 'framer-motion'

export interface Product {
  id: number
  price: number
  thumbnail: string
  description: string
  category: {
    id: number
    categoryName: string
  }
  brand: {
    id: number
    brandName: string
  }
  variant: Variant[]
  created_at: string
  updated_at: string
  productName: string
  productImages: string[]
}

export interface ProductListType {
  products: Product[]
  pagination: {
    page: number
    limit: number
    page_size: number
  }
}

export interface ProductListParam {
  page?: number
  limit?: number
  sort?: 'Newest' | 'PriceDESC' | 'PriceASC'
  category?: number
  brand?: number
  keyword?: string
  minPrice?: number
  maxPrice?: number
  size?: number
  color?: number
}
