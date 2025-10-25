import { Button } from "../ui/button";
import { ShoppingCartIcon } from "lucide-react";
import { useAuth } from "@/hooks/useAuth";

type AddToCartProps = {
  quantity: number;
  variant?: string | null;
};

const AddToCart = ({ quantity, variant }: AddToCartProps) => {
  const { auth } = useAuth();
  
  const onsubmit = () =>{}

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
