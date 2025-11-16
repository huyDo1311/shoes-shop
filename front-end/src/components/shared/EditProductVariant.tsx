import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog";
import { useEffect, useState } from "react";
import { toast } from "sonner";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import ProductVariantForm from "@/components/shared/ProductVariantForm";
import { type ProductVariantBodyType, type Variant } from '@/types/variant.type';
import variantApi from "@/apis/variant.api";

type CreateProductItemProps = {
  productId: number;
  variant: Variant;
};
const EditProductVariant = ({
  productId,
  variant,
}: CreateProductItemProps) => {
  // const [defaultValues, setDefaultValues] = useState(null);
  const [defaultValues, setDefaultValues] = useState<Omit<ProductVariantBodyType, "productId"> | null>(null);
  const queryClient = useQueryClient();

  const { mutate: updateVariant, isPending } = useMutation({
    mutationFn: (body: ProductVariantBodyType) =>
      variantApi.updateVariant(variant.sku, body),
    onSuccess: () => {
      toast.success("Update variant successfully!");
      queryClient.invalidateQueries({ queryKey: ["product-detail"]});
      queryClient.refetchQueries({ queryKey: ["product-detail", productId] });
    },
    onError: () => {
      toast.error("Failed to update variant");
    },
  });

  useEffect(() => {
    if (variant) {
      setDefaultValues({
        quantity: variant.quantity,
        sizeId: parseInt(variant?.size) ,
        colorId: parseInt(variant?.color)
      });
    }
  }, [variant]);
  

  console.log('variant', variant)

  const onSubmit = (data: Omit<ProductVariantBodyType, "productId">) => {
  updateVariant({
    ...data,
    productId,  
    sku: variant.sku, 
  });
};
  return (
    <Dialog>
      <DialogTrigger asChild>
        <p className="text-sky-400 hover:underline cursor-pointer">Edit</p>
      </DialogTrigger>
      <DialogContent className="sm:max-w-[425px]">
        <DialogHeader>
          <DialogTitle>Edit product item</DialogTitle>
        </DialogHeader>
        {defaultValues && (
          <ProductVariantForm
            defaultValues={defaultValues}
            loading={isPending}
            onSubmit={onSubmit}
          />
        )}
      </DialogContent>
    </Dialog>
  );
};

export default EditProductVariant;
