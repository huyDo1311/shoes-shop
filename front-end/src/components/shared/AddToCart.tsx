import { Button } from "../ui/button";
import { ShoppingCartIcon } from "lucide-react";
import { useAuth } from "@/hooks/useAuth";
import { useContext } from "react";
import AuthContext from "@/context/AuthContext";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { orderAPI } from "@/apis/order.api";
import { toast } from "sonner";
import { useNavigate } from "react-router-dom";

export type AddToCartProps = {
  quantity: number;
  sku?: string | null;
  email?: string
};

const AddToCart = ({ quantity, sku }: AddToCartProps) => {
  const navigate = useNavigate();
  const { currentUser } = useAuth();
  const email = currentUser?.email;

  const queryClient = useQueryClient();

  const addToCartMutation = useMutation({
    mutationFn: (body: AddToCartProps) => orderAPI.addToCart(body),
    onSuccess: (result) => {
      const cart = result.data.data;
      const sortedItems = [...(cart?.items || [])].sort((a, b) => a.id - b.id);
      const newItem = sortedItems[sortedItems.length - 1];

      toast.success(
        `Added "${newItem?.productName || 'item'}" to cart successfully!`,
        { description: `Quantity: ${newItem?.quantity}` }
      );
      queryClient.invalidateQueries({ queryKey: ["cart", email] });
    },
  })

  const onsubmit = () => {
    if (!email) {
      toast.warning("Please sign in to add items to your cart.");
      navigate("/sign-in");
      return;
    }
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
