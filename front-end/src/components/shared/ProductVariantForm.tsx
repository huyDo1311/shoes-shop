// import { productItemSchema, ProductItemType } from "@/zod";
import { zodResolver } from "@hookform/resolvers/zod";
import { useForm, type Resolver } from 'react-hook-form';
import { Form } from "../ui/form";
import { cn } from "@/lib/utils";
import RenderFormField from "./RenderFormField";
// import useFetchData from "@/hooks/useFetchData";
import RenderFormSelect from "./RenderFormSelect";
import { Button } from "../ui/button";
import { productVariantSchema, type ProductVariantType, type ProductVariantBodyType, productVariantForm } from '@/types/variant.type';
import { useQuery } from '@tanstack/react-query';
import colorApi from '@/apis/color.api';
import sizeApi from '@/apis/size.api';
import { useEffect, useState } from 'react';


interface ProductItemFormProps {
  defaultValues?: ProductVariantBodyType;
  loading: boolean;
  className?: string;
  onSubmit: (data: ProductVariantBodyType) => void;
}

const ProductVariantForm = ({
  loading,
  onSubmit,
  className,
  defaultValues,
}: ProductItemFormProps) => {
  const {
    data: colors,
    isLoading: isColorLoading,
    isError: isColorError,
  } = useQuery({
    queryKey: ['color'],
    queryFn: async () => {
      const result = await colorApi.getColor()
      return result.data.data
    },
  });

  const {
    data: sizes,
    isSuccess: isSizeSuccess,
    isLoading: isSizeLoading,
    isError: isSizeError
  } = useQuery({
    queryKey: ['size'],
    queryFn: async () => {
      const result = await sizeApi.getSize()
      return result.data.data
    },
  });

  const [sizeOptions, setSizeOptions] = useState<{ value: string; label: number }[] | null>(null);

  // genarate  options
  useEffect(() => {
    if (isSizeSuccess) {
      const sizeOptionsMatching = sizes.map((size) => ({
        value: size.id.toString(),
        label: size.sizeValue,
      }));
      setSizeOptions(sizeOptionsMatching);
    }
  }, [sizes, isSizeSuccess]);


  const form = useForm<ProductVariantBodyType>({
    resolver: zodResolver(productVariantForm) as unknown as Resolver<ProductVariantBodyType>,
    defaultValues,
  });

  const values = form.watch();
  console.log("Form values:", values);

  const { handleSubmit, watch, formState: { errors } } = form;
  // console.log("Form errors:", errors);

  if (isColorLoading || isSizeLoading) return <div>Loading...</div>;
  if (isColorError || isSizeError) return <div>Error...</div>;

  return (
    <Form {...form}>
      <form
        onSubmit={form.handleSubmit(onSubmit)}
        className={cn("space-y-4", className)}
      >
        <RenderFormField
          control={form.control}
          type="input"
          name="quantity"
          title="Quantity"
          inputType="number"
        />

        {/* SIZE SELECT */}
        <RenderFormSelect
          control={form.control}
          name="sizeId"
          title="Size"
          options={
            sizes?.map(s => ({
              id: s.id.toString(),
              value: s.sizeValue.toString()
            })) ?? []
          }
          type="key-value"
          valueKey="id"
          displayKey="value"
        />

        {/* COLOR SELECT */}
        <RenderFormSelect
          control={form.control}
          name="colorId"
          title="Color"
          options={
            colors?.map(c => ({
              id: c.id.toString(),
              value: c.colorName
            })) ?? []
          }
          type="key-value"
          valueKey="id"
          displayKey="value"
        />
        <Button type="submit" disabled={loading}>
          {loading ? "Loading..." : "Submit"}
        </Button>
      </form>
    </Form>
  );
};

export default ProductVariantForm;
