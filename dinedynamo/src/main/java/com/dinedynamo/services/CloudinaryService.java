package com.dinedynamo.services;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import com.cloudinary.Transformation;

@Service
public class CloudinaryService
{
    @Autowired
    Cloudinary cloudinary;

    /**
     *
     * @param bytesOfImage - byte array
     * @return Map object
     * @throws IOException
     * uploads image on cloudinary
     */
    public Map uploadImageOnCloudinary(byte[] bytesOfImage) throws IOException {

        Map dataOfImage = cloudinary.uploader().upload(bytesOfImage,Map.of());

        return dataOfImage;

        //Image url in map.get("secure_url") or map.get("url")

    }

    /**
     *
     * @param publicId
     * @return
     * @throws Exception
     */
    public String getImageURLFromPublicId(String publicId) throws Exception {
        Map result = cloudinary.api().resource(publicId,null);

        if(result == null)  //means the image does not exist in cloudinary
            return null;


        return cloudinary.url().transformation(new Transformation().fetchFormat("png")).generate(publicId);


    }

    /**
     *
     * @param publicId
     * @throws IOException
     * deletes the image from cloudinary
     */
    public void deleteImageFromCloudinary(String publicId) throws IOException {
        Map result = cloudinary.uploader().destroy(publicId, null);
        // The result map will contain information about the deletion
        System.out.println("Deletion result: " + result);
    }

}
