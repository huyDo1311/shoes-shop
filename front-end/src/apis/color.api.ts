import type { Color } from '@/types/color.type';
import type { SuccessResponse } from '@/types/utils.type';
import http from '@/utils/http';



const URL_COLOR = 'colors'

const colorApi = {
  getColor: () => http.get<SuccessResponse<Color[]>>(URL_COLOR)
}

export default colorApi