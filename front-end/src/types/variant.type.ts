import {z} from 'zod';
export interface Variant {
  sku: string
  quantity: number
  color: string
  size: string
  productId?: number
}

export type VariantResponse = {
    sku: string;
    quantity: number;
    color: string;
    size: string;
    productName: string;
}

export const productVariantSchema = z.object({
  productId: z.number(),
  quantity: z.number().positive(),
  color: z.array(z.string()),
  size: z.array(z.string()),
});

export type ProductVariantType = z.infer<typeof productVariantSchema>;

export const productVariantForm = z.object({
  // productId: z.number(),
  // quantity: z.number(),  
  // colorId: z.number(),                     
  // sizeId: z.number(),    
  productId: z.coerce.number().optional(),
  quantity: z.coerce.number().positive(),
  colorId: z.coerce.number(),
  sizeId: z.coerce.number(),      
  sku: z.string().optional(),            
});

export type ProductVariantBodyType = z.infer<typeof productVariantForm>;

export type VariantUpdateResponse = {
    sku: string;
    quantity: number;
    color: string;
    size: string;
    productName: string;
}
