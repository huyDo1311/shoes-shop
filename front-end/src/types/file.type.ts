export type FileResponse = {
    uploadTime: number;
    fileName: string;
    publicUrl: string;
    size: number;
}

export type MultipleFileResponse = FileResponse[]; 