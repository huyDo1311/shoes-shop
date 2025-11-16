import { useParams } from "react-router-dom";
import CreateProductVariant from "@/components/shared/CreateProductVariant";

const CreateProductVariantPage = () => {
  const { productId } = useParams<{ productId: string }>();
  if (!productId) return <div>Product ID is missing</div>;
  
  return <div className="">
    <h1>Create Product Variant</h1>
    <CreateProductVariant productId={productId} />
   </div>
};

export default CreateProductVariantPage;
