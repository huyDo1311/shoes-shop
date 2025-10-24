import { cn } from "@/lib/utils";
import { zodResolver } from "@hookform/resolvers/zod";
import { useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import { Form } from "../ui/form";
import RenderFormField from "./RenderFormField";
import RenderFormSelect from "./RenderFormSelect";
import { Button } from "../ui/button";
import { Slider } from "../ui/slider";
import { searchSchema, type SearchType } from "@/types/filter.type";
import { useQuery } from "@tanstack/react-query";
import categoryApi from "@/apis/category.api";
import brandApi from "@/apis/brand.api";
import colorApi from "@/apis/color.api";
import sizeApi from "@/apis/size.api";

type SearchFormProps = {
  className?: string;
  defaultValues?: SearchType;
  onSubmit: (data: SearchType) => void;
};

const SearchForm = ({
  className,
  defaultValues,
  onSubmit,
}: SearchFormProps) => {
  const form = useForm<SearchType>({
    resolver: zodResolver(searchSchema),
    defaultValues,
  });

  const {
    data: categories,
    isLoading: isCategoryLoading,
    isError: isCategoryError,
  } = useQuery({
    queryKey: ['category'],
    queryFn: async () => {
      const result = await categoryApi.getCategories()
      return result.data.data
    },
  });

  const {
    data: brands,
    isLoading: isBrandLoading,
    isError: isBrandError,
  } = useQuery({
    queryKey: ['brand'],
    queryFn: async () => {
      const result = await brandApi.getBrand()
      return result.data.data
    },
  });

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

  // price slider
  const [priceValues, setPriceValues] = useState([
    defaultValues?.minPrice || 100000,
    defaultValues?.maxPrice || 2000000,
  ]);

  // const [priceValues, setPriceValues] = useState<number[]>(() => {
  //   const min = defaultValues?.minPrice ?? 0;
  //   const max = defaultValues?.maxPrice ?? 50_000_000;
  //   return min === max ? [0, 50_000_000] : [min, max];
  // });

  // handle price slider change

  const handleValueChange = (newValues: any) => {
    setPriceValues(newValues);
    form.setValue("minPrice", newValues[0]);
    form.setValue("maxPrice", newValues[1]);
  };

  if (isCategoryLoading || isBrandLoading || isColorLoading || isSizeLoading)
    return <div>Loading...</div>;
  if (isCategoryError || isBrandError || isColorError || isSizeError)
    return <div>Error</div>;




  return (
    <Form {...form}>
      <form
        onSubmit={form.handleSubmit(onSubmit)}
        className={cn(" px-2 space-y-5", className)}
      >
        <RenderFormField
          control={form.control}
          name="keyword"
          title="Keyword"
          inputType="text"
          type="input"
        />
        <RenderFormSelect
          control={form.control}
          name="category"
          title="Category"
          // options={[{ id: "all", name: "All" }, ...(categories ?? [])]}
          options={[
            { id: "all", name: "All" },
            ...(categories?.map((c) => ({
              id: c.id.toString(),
              name: c.categoryName,
            })) ?? []),
          ]}
          type="key-value"
          displayKey="name"
          valueKey="id"
        />
        <RenderFormSelect
          control={form.control}
          name="brand"
          title="Brand"
          // options={[{ id: "all", name: "All" }, ...(brands ?? [])]}
          options={[
            { id: "all", name: "All" },
            ...(brands?.map((b) => ({
              id: b.id.toString(),
              name: b.brandName,
            })) ?? []),
          ]}
          type="key-value"
          displayKey="name"
          valueKey="id"
        />
        <RenderFormSelect
          control={form.control}
          name="color"
          title="Color"
          options={
            colors
              ? colors.map((color) => ({
                value: color.id.toString(),
                label: color.colorName,
              }))
              : []
          }
          type="multi"
        />
        {sizeOptions && (
          <RenderFormSelect
            control={form.control}
            name="size"
            title="Size"
            options={sizeOptions}
            type="multi"
          />
        )}
        <Slider
          defaultValue={priceValues}
          max={2000000}
          min={100000}
          step={10000}
          onValueChange={handleValueChange}
          className={cn("w-full !mt-8")}
        />
        <div className="flex justify-between">
          <p>
            Price: <span className="font-semibold">${priceValues[0]}</span> -{" "}
            <span className="font-semibold">${priceValues[1]}</span>
          </p>
        </div>
        <div className="flex gap-3 justify-end">
          <Button>Filter</Button>
        </div>
      </form>
    </Form>
  );
};

export default SearchForm;
