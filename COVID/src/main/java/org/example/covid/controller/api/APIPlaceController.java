package org.example.covid.controller.api;

import org.example.covid.constant.PlaceType;
import org.example.covid.dto.APIDataResponse;
import org.example.covid.dto.PlaceDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api")
@RestController
public class APIPlaceController {

    @GetMapping("/places")
    public APIDataResponse<List<PlaceDto>> getPlaces() {
        return APIDataResponse.of(
                List.of(
                        PlaceDto.of(
                                PlaceType.COMMON,
                                "장소이름",
                                "주소내용",
                                "010-1234-5678",
                                30,
                                "메모내용"
                        )
                )
        );
    }

    @PostMapping("/places")
    public Boolean createPlace() {
        return true;
    }

    @GetMapping("/places/{placeId}")
    public APIDataResponse<PlaceDto> getPlace(@PathVariable Integer placeId) {

        if(placeId.equals(2)){
            return APIDataResponse.of(null);
        }

        return APIDataResponse.of(PlaceDto.of(
                PlaceType.COMMON,
                "장소이름",
                "주소내용",
                "010-1234-5678",
                30,
                "메모내용"
        ));
    }

    @PutMapping("/places/{placeId}")
    public Boolean modifyPlace(@PathVariable Integer placeId) {
        return true;
    }

    @DeleteMapping("/places/{placeId}")
    public Boolean removePlace(@PathVariable Integer placeId) {
        return true;
    }
}
