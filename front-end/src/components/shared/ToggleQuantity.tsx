import React, { useState } from "react";
import QuantityInput from "./QuantityInput";
import { toast } from "sonner";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { useAuth } from "@/hooks/useAuth";
import { orderAPI } from "@/apis/order.api";

// type ToggleQuantityProps = {
//   cartId: number;
//   max: number;
//   quantity: number;
//   onQuantityChange: (v: number) => void;
// };

type ToggleQuantityProps = {
  cartId: number;
  max: number;
  quantity: number;
  sku: string;
  onQuantityChange: (v: number) => void;
};

const ToggleQuantity = ({
  max,
  cartId,
  quantity,
  sku,
  onQuantityChange,
}: ToggleQuantityProps) => {
  // const [currentQuantity, setCurrentQuantity] = useState(quantity);
  // // console.log(cartId)
  // const queryClient = useQueryClient();
  

  
  // const onChange = (v: number) => {
 
  // };

  const [currentQuantity, setCurrentQuantity] = useState(quantity);
  const queryClient = useQueryClient();
  const { currentUser } = useAuth();
  const email = currentUser?.email;

  // Mutation để cập nhật quantity
  const updateQuantityMutation = useMutation({
    mutationFn: (newQuantity: number) =>
      orderAPI.updateItemQuantity({
        email: email as string,
        sku,
        quantity: newQuantity,
      }),
    onSuccess: () => {
      // toast.success("Cập nhật số lượng thành công!");
      queryClient.invalidateQueries({ queryKey: ["cart", email] });
    },
    onError: () => {
      toast.error("Không thể cập nhật số lượng!");
      setCurrentQuantity(quantity); // hoàn tác nếu lỗi
    },
  });

  const onChange = (v: number) => {
    if (v < 1) return toast.warning("Số lượng tối thiểu là 1!");
    if (v > max) return toast.warning(`Chỉ còn tối đa ${max} sản phẩm!`);

    setCurrentQuantity(v);
    onQuantityChange(v);

    // Gọi API cập nhật
    updateQuantityMutation.mutate(v);
  };

  return (
    <QuantityInput
      max={max}
      // loading={isPending}
      onValueChange={onChange}
      quantity={currentQuantity}
      setQuantity={setCurrentQuantity}
    />
  );
};

export default ToggleQuantity;
