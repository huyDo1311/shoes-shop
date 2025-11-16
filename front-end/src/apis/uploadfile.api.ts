import type { FileResponse, MultipleFileResponse } from "@/types/file.type";
import type { SuccessResponse } from "@/types/utils.type";
import http from "@/utils/http";

export const fileAPI = {
  uploadFile: (file: File) => {
    const formData = new FormData()
    formData.append('file', file)
    return http.post<FileResponse>('/files', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })
  },

  uploadMultiple: (files: FileList | File[]) => {
    const formData = new FormData();
    Array.from(files).forEach((file) => formData.append("files", file));

    return http.post<FileResponse[]>("/files/multiple", formData, {
      headers: { "Content-Type": "multipart/form-data" },
    });
  },
}
