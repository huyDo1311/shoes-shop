// import { createContext, useState } from "react";
// import { Navigate, useLocation } from "react-router-dom";
// import { useAuth } from "@/hooks/useAuth";
// import type { CartItem } from "@/types/order.type";

// interface CheckoutType {
//   products: CartItem[] | null;
//   updateProducts: (newProducts: CartItem[]) => void;
//   deleteProducts: () => void;
//   total: number;
//   setTotalCost: (newTotal: number) => void;
// }

// export const CheckoutContext = createContext<CheckoutType | undefined>(undefined);

// const CheckoutProvider = ({ children }: { children: React.ReactNode }) => {
//   const auth = useAuth();
//   const location = useLocation();

//   const [products, setProducts] = useState<CartItem[] | null>(
//     JSON.parse(localStorage.getItem("check_out_products") || "null")
//   );
//   const [total, setTotal] = useState<number>(
//     JSON.parse(localStorage.getItem("total") || "0")
//   );

//   if (!auth.auth) {
//     return <Navigate to="/login" replace state={{ from: location }} />;
//   }

//   function updateProducts(newProducts: CartItem[]) {
//     setProducts(newProducts);
//     localStorage.setItem("check_out_products", JSON.stringify(newProducts));
//   }

//   function setTotalCost(newTotal: number) {
//     setTotal(newTotal);
//     localStorage.setItem("total", JSON.stringify(newTotal));
//   }

//   function deleteProducts() {
//     setProducts(null);
//     localStorage.removeItem("check_out_products");
//     localStorage.removeItem("total");
//   }

//   return (
//     <CheckoutContext.Provider
//       value={{ products, updateProducts, deleteProducts, total, setTotalCost }}
//     >
//       {children}
//     </CheckoutContext.Provider>
//   );
// };

// export default CheckoutProvider;
