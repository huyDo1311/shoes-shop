import type { Category } from "@/types/category.type"
import type { SuccessResponse } from "@/types/utils.type"
import http from "@/utils/http"


const URL_CATEGORY = 'categories'

const categoryApi = {
  getCategories: () => http.get<SuccessResponse<Category[]>>(URL_CATEGORY)
}

export default categoryApi