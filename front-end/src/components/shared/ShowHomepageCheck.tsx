import { Checkbox } from "../ui/checkbox";
import { toast } from "sonner";
import { useQueryClient } from "@tanstack/react-query";

type ShowHomepageCheckProps = {
  id: number;
  showHomepage: boolean;
};

const ShowHomepageCheck = ({ id, showHomepage }: ShowHomepageCheckProps) => {
  const queryClient = useQueryClient();

  const handleCheck = (e: boolean) => {
  };
  return (
    <Checkbox
      // disabled={isPending}
      checked={showHomepage}
      onCheckedChange={(e) => handleCheck(e as boolean)}
    />
  );
};

export default ShowHomepageCheck;
