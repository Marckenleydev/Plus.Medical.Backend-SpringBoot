package marc.dev.DoctorBooking_appointement.service;




import org.springframework.web.multipart.MultipartFile;


public interface CloudinaryService {
    /**
     * Uploads a file to Cloudinary and returns the URL of the uploaded file.
     *
     * @param file       the file to be uploaded
     * @param folderName the folder in Cloudinary where the file will be stored
     * @return the URL of the uploaded file
     */
    String uploadFile(MultipartFile file, String folderName);
}
