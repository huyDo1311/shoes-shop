import type { Variant } from '@/types/variant.type'
import type { NumberMap } from 'framer-motion'
import {z} from 'zod';

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

// export interface Product {
//   id: number;
//   productName?: string;
//   price?: number;
//   thumbnail?: string;
//   description?: string;
//   category?: {
//     id: number;
//     categoryName: string;
//   };
//   brand?: {
//     id: number;
//     brandName: string;
//   };
//   productImages?: string[];
//   variant?: {
//     sku: string;
//     color: string;
//     size: string;
//     quantity: number;
//   }[];
//   created_at?: string;
//   updated_at?: string;
// }


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


// export const productSchema = (type: "create" | "update") =>
//   z.object({
//     name: z.string({ message: "Product name is required" }),
//     description: z.string({ message: "Description is required" }),
//     thumbnail:
//       type === "create"
//         ? z.instanceof(FileList, { message: "Avatar is required" })
//         : z.union([z.instanceof(FileList).optional(), z.string().optional()]),
//     images:
//       type === "create"
//         ? z.instanceof(FileList, { message: "Product images are required" })
//         : z.union([z.instanceof(FileList).optional(), z.array(z.string()).optional()]),
//     categoryId: z.string({ message: "Category is required" }),
//     brandId: z.string({ message: "Brand is required" }),
//     price: z.preprocess(
//       (val) => {
//         if (typeof val === "string") return parseFloat(val);
//         if (typeof val === "number") return val;
//         return undefined;
//       },
//       z.number({ message: "Price must be a number" }).positive("Price must be positive")
//     ),
//     productColorId: z.string({ message: "Color is required" }),
//   });

// // Type tự động từ schema, dùng cho RHF
// export type ProductFormType = z.infer<ReturnType<typeof productSchema>>;

// export const productSchema = (type: "create" | "update") =>
//   z.object({
//     name: z.string(),
//     description: z.string(),
//     thumbnail:
//       type === "create"
//         ? z.instanceof(FileList, { message: "Thumbnail is required" })
//         : z.instanceof(FileList).or(z.string()),
//     images:
//       type === "create"
//         ? z.instanceof(FileList, { message: "Product images are required" })
//         : z.instanceof(FileList).optional().or(z.array(z.string()).optional()),
//     categoryId: z.string(),
//     brandId: z.string(),
//     price: z.coerce.number().positive("Price must be a positive number"),
//     productColorId: z.string(),
//   });

// export type ProductFormType = z.infer<ReturnType<typeof productSchema>>;

export const productSchema = (type: "create" | "update") =>
  z.object({
    productName: z.string(),
    description: z.string(),
    thumbnail:
      type === "create"
        ? z.instanceof(FileList, { message: "Thumbnail is required" })
        : z.instanceof(FileList).or(z.string()),
    images:
      type === "create"
        ? z.instanceof(FileList, { message: "Product images are required" })
        : z.instanceof(FileList).optional().or(z.array(z.string()).optional()),
    categoryId: z.string(),
    brandId: z.string(),
    price: z.coerce.number().positive("Price must be a positive number"),
  });

export type ProductFormType = z.infer<ReturnType<typeof productSchema>>;

// export const createProductSchema = z.object({
//   name: z.string(),
//   description: z.string(),
//   thumbnail: z.instanceof(FileList, { message: "Thumbnail is required" }),
//   images: z.instanceof(FileList, { message: "Product images are required" }),
//   categoryId: z.string(),
//   brandId: z.string(),
//   price: z.coerce.number().positive("Price must be positive"),
//   productColorId: z.string(),
// });

// export const updateProductSchema = z.object({
//   name: z.string(),
//   description: z.string(),
//   thumbnail: z.instanceof(FileList).or(z.string()),
//   images: z.instanceof(FileList).optional().or(z.array(z.string()).optional()),
//   categoryId: z.string(),
//   brandId: z.string(),
//   price: z.coerce.number().positive("Price must be positive"),
//   productColorId: z.string(),
// });

export interface BodyCreateProduct  {
    productName: string
    description: string
    thumbnail: string
    images: string[]
    categoryId: string
    brandId: string
    colorId?: string
    price: number
}
