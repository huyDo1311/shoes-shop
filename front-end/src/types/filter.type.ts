import {z} from 'zod';
export const searchSchema = z.object({
  keyword: z.string().optional(),
  category: z.string().optional(),
  brand: z.string().optional(),
  color: z.array(z.string()).optional(),
  size: z.array(z.string()).optional(),
  minPrice: z.number().optional(),
  maxPrice: z.number().optional(),
});

export type SearchType = z.infer<typeof searchSchema>;