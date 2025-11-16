import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog";
import { Button } from "../ui/button";
import ProductItemForm from "./ProductVariantForm";
// import { ProductItemType } from "@/zod";
import { toast } from "sonner";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import {type ProductVariantBodyType} from '@/types/variant.type';
import variantApi from "@/apis/variant.api";
import { useNavigate } from "react-router-dom";

type CreateProductItemProps = {
  productId: string;
};

const CreateProductVariant = ({ productId }: CreateProductItemProps) => {
  const queryClient = useQueryClient();
  const navigate = useNavigate();
  const {
    mutate: createVariant,
    isPending,
  } = useMutation({
    mutationFn: (body: ProductVariantBodyType) => variantApi.createVariant(body),
    onSuccess: () => {
      toast.success("Create variant successfully!");
      queryClient.invalidateQueries({queryKey: ["product-detail", productId]});
      navigate(`/admin/product/${productId}/variant`)
    },
    onError: () => {
      toast.error("Failed to create variant");
    },
  });

  const onSubmit = (data: ProductVariantBodyType) => {
    console.log("aasfsadf");
    createVariant({
      ...data,
      productId: Number(productId) 
    });
    console.log("Submit data:", data);
  };
  return (
    <Dialog>
      <DialogTrigger asChild>
        <Button size="sm">Create new</Button>
      </DialogTrigger>
      <DialogContent className="sm:max-w-[425px]">
        <DialogHeader>
          <DialogTitle>Create new product variant</DialogTitle>
          <DialogDescription>
            Fill in the form below to create a new product item
          </DialogDescription>
        </DialogHeader>
        <ProductItemForm loading={isPending} onSubmit={onSubmit} />
      </DialogContent>
    </Dialog>
  );
};

export default CreateProductVariant;
