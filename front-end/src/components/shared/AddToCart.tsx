import { Button } from "../ui/button";
import { ShoppingCartIcon } from "lucide-react";
import { useAuth } from "@/hooks/useAuth";
import { useContext } from "react";
import AuthContext from "@/context/AuthContext";
import { useMutation } from "@tanstack/react-query";
import { orderAPI } from "@/apis/order.api";

export type AddToCartProps = {
  quantity: number;
  sku?: string | null;
  email?: string 
};

const AddToCart = ({ quantity, sku }: AddToCartProps) => {
  const { currentUser } = useAuth();
  const email = currentUser?.email;

  const addToCartMutation = useMutation({
    mutationFn: (body: AddToCartProps) => orderAPI.addToCart(body),
    onSuccess: (result) => {
      const cart = result.data.data;
    }
  })
 
  const onsubmit = () => {
    addToCartMutation.mutate({
      quantity: quantity,
      sku: sku,
      email: email
    })
   }

  return (
    <Button
      disabled={quantity < 1}
      onClick={onsubmit}
      className="blue-button"
      size="lg"
    >
      <ShoppingCartIcon className="mr-2 size-4" />
      Add to cart
    </Button>
  );
};

export default AddToCart;
