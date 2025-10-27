export const OrderStatus = {
  Pending: "Pending",
  Confirmed: "Confirmed",
  Shipped: "Shipped",
  Delivered: "Delivered",
  Cancelled: "Cancelled",
} as const;

export interface CartItem {
  id: number;
  sku: string;
  productId: number;
  productName: string;
  imageUrl: string | null;
  quantity: number;
  price: number;
  lineTotal: number;
}

export interface Cart {
  id: number;
  userEmail: string;
  status: keyof typeof OrderStatus;
  total: number;
  items: CartItem[];
}
