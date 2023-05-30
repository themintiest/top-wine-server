package vn.topwines.core.storage;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import vn.topwines.core.constants.Roles;
import vn.topwines.core.utils.MultipartFileUtils;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.StreamingOutput;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

@Path("/storages")
@Tag(name = "Storages")
@ApplicationScoped
@RequiredArgsConstructor
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class StorageResource {
    private final FileService fileService;

    @POST
    @Path("/upload")
    @RolesAllowed(Roles.ADMIN)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Operation(summary = "Upload file to storage", description = "Upload file to storage")
    public String uploadFile(@QueryParam("fileName") String fileName, @MultipartForm MultipartFormDataInput multipartForm) {
        String name = StringUtils.isNotBlank(fileName) ? fileName : getFileNameFromMultipart(multipartForm);
        try (InputStream inputStream = multipartForm.getFormDataPart("file", InputStream.class, null)) {
            return fileService.uploadFile(name, inputStream);
        } catch (IOException exception) {
            throw new BadRequestException(exception.getMessage());
        }
    }

    @GET
    @PermitAll
    @Path("/download")
    @Operation(summary = "Download file from storage", description = "Download file from storage")
    public Response download(@QueryParam("fileName") String fileName) {
        java.nio.file.Path filePath = Paths.get(fileService.getFilePath(fileName));
        String name = filePath.getFileName().toString();
        return Response.ok()
                .header("Content-Type", MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", "attachment;filename=" + name)
                .entity((StreamingOutput) output -> Files.copy(filePath, output))
                .build();
    }

    private String getFileNameFromMultipart(MultipartFormDataInput multipartFormDataInput) {
        return MultipartFileUtils.getFileName(multipartFormDataInput.getFormDataMap().get("file").get(0));
    }
}
