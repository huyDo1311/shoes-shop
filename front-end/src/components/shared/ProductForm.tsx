import { type BodyCreateProduct, type Product, type ProductFormType, productSchema } from "@/types/product.type";
import { zodResolver } from "@hookform/resolvers/zod";
import { useForm, type Resolver } from "react-hook-form";
import { Form } from "../ui/form";
import RenderFormField from "./RenderFormField";
import { cn } from "@/lib/utils";
import RenderFormSelect from "./RenderFormSelect";
import { Button } from "../ui/button";
import RenderFormUpload from "./RenderFormUpload";
import { toast } from "sonner";
import { useState } from "react";
import { useQueryClient, useQuery, useMutation } from '@tanstack/react-query';
import { useNavigate } from "react-router-dom";
import type z from "zod";
import colorApi from '@/apis/color.api';
import categoryApi from '@/apis/category.api';
import brandApi from '@/apis/brand.api';
import { fileAPI } from "@/apis/uploadfile.api";
import type { FileResponse } from "@/types/file.type";
import productApi from "@/apis/product.api";
import CreateProductVariant from "@/components/shared/CreateProductVariant";
// import type z from "zod";

type ProductFormProps = {
  defaultValues?: ProductFormType;
  className?: string;
  type: "create" | "update";
  id?: string;
  images?: string[];
  setImages?: (images: string[]) => void;
};

const ProductForm = ({
  type,
  id,
  defaultValues,
  images,
  className,
  setImages,
}: ProductFormProps) => {


  const form = useForm<ProductFormType>({
    resolver: zodResolver(productSchema(type)) as unknown as Resolver<ProductFormType>,
    defaultValues,
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

  const queryClient = useQueryClient();

  const navigate = useNavigate();

  const [loading, setLoading] = useState(false);

  // let uploadedImages: string[] = [];

  const uploadFileMutation = useMutation({
    mutationFn: (file: File) => fileAPI.uploadFile(file),
  })

  const uploadMutipleFilesMutation = useMutation({
    mutationFn: (files: FileList | File[]) => fileAPI.uploadMultiple(files),
  })

  const {
    mutateAsync: createProduct,
    isPending: isCreating,
  } = useMutation({
    mutationFn: (body: BodyCreateProduct) => productApi.createProduct(body),
  });

  const {
    mutateAsync: updateProduct,
    isPending: isUpdating,
  } = useMutation({
    mutationFn: ({ id, body }: { id: string; body: BodyCreateProduct }) =>
      productApi.updateProduct(id, body),
  });



  const handleSubmit = async (formData: ProductFormType) => {
    // const body = { ...formData };

    // console.log(body);
    try {
      setLoading(true);

      let uploadedThumbnail = "";
      let uploadedImages: string[] = [];

      //upload file
      if (formData.thumbnail.length > 0) {
        if (formData.thumbnail instanceof FileList) {
          const file = formData.thumbnail[0];
          // console.log('formData.thumbnail', formData.thumbnail)
          // console.log('file', file)
          try {
            const result = await uploadFileMutation.mutateAsync(file);
            // console.log(result);
            uploadedThumbnail = result?.data?.publicUrl;
            // console.log('uploadedThumbnail', uploadedThumbnail)
          } catch (error) {
            console.log(error);
          }
        }
      }

      //upload multiple file
      if (formData.images && formData.images.length > 0) {
        if (formData.images instanceof FileList) {
          // console.log('formData.images', formData.images)
          try {
            const result = await uploadMutipleFilesMutation.mutateAsync(formData.images)
            // console.log('results', result)
            // urls = result.data.data.map((item: FileResponse) => item.publicUrl)
            uploadedImages = result?.data?.map((item: FileResponse) => item.publicUrl);
            // console.log('uploadedImages', uploadedImages)
          } catch (error) {
            console.log(error);
          }

        }
      }

      const body: BodyCreateProduct = {
        productName: formData.productName,
        description: formData.description,
        price: formData.price,
        categoryId: formData.categoryId,
        brandId: formData.brandId,
        thumbnail: uploadedThumbnail,     // đã upload xong
        images: [...(images || []), ...uploadedImages], // merge với existing images nếu có
      };

      // Call API


      if (type === "create") {
        await createProduct(body, {
          onSuccess: (res) => {
            toast.success("Product created successfully");
            queryClient.invalidateQueries({ queryKey: ["products"] });
            // navigate("/admin/manage-product");

            const newProductId = res.data?.data.id;
            if (newProductId) {
              navigate(`/admin/create-product-variant/${newProductId}`);
            } else {
              navigate("/admin/manage-product");
            }
          },
          onError: () => toast.error("Error creating product"),
        });
      } else if (type === "update" && id) {
        await updateProduct(
          { id, body },
          {
            onSuccess: () => {
              toast.success("Product updated successfully");
              queryClient.invalidateQueries({ queryKey: ["products"] });
              navigate("/admin/manage-product");
            },
            onError: () => toast.error("Error updating product"),
          }
        );
      }



    } catch (error) {
      console.error("Error creating product", error);
      toast.error("Error creating product");
    } finally {
      setLoading(false);
    }
  };

  if (isBrandError || isCategoryError || isColorError) {
    return <div>Error</div>;
  }
  if (isBrandLoading || isCategoryLoading || isColorLoading) {
    return <div>Loading...</div>;
  }

  const isPending = loading || isCreating || isUpdating;

  return (
    <Form {...form}>
      <form
        onSubmit={form.handleSubmit(handleSubmit)}
        className={cn(className)}
      >
        <div className="grid grid-cols-2 gap-2">
          <RenderFormField
            name="productName"
            title="Product Name"
            type="input"
            control={form.control}
          />
          <RenderFormField
            name="price"
            title="Price"
            type="input"
            control={form.control}
          />
        </div>
        <div className="grid grid-cols-2 gap-2">
          <RenderFormUpload
            name="thumbnail"
            control={form.control}
            title="Thumbnail"
            type="input"
            isMultible={false}
          />
          <RenderFormUpload
            name="images"
            control={form.control}
            title="Product Images"
            type="input"
            isMultible
          />
        </div>


        <div className="flex items-center gap-x-2">
          {/* <RenderFormSelect
            name="colorId"
            title="Color"
            control={form.control}
            options={colors || []}
            valueKey="id"
            displayKey="colorName"
            className="w-1/3"
          /> */}
          <RenderFormSelect
            name="categoryId"
            title="Category"
            control={form.control}
            options={categories || []}
            valueKey="id"
            className="w-1/2"
            displayKey="categoryName"
          />
          <RenderFormSelect
            control={form.control}
            options={brands || []}
            valueKey="id"
            displayKey="brandName"
            name="brandId"
            title="Brand"
            className="w-1/2"
          />
        </div>
        <RenderFormField
          name="description"
          control={form.control}
          title="Description"
          type="text-editor"
        />

        <Button disabled={isPending} type="submit">
          {isPending ? "Loading..." : type === "create" ? "Create Product" : "Update Product"}
        </Button>
      </form>
    </Form>
  );
};

export default ProductForm;
