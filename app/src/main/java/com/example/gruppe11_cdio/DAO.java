package com.example.gruppe11_cdio;

import com.example.gruppe11_cdio.Objects.GameBoard;
import com.example.gruppe11_cdio.Objects.ImgDTO;
import com.example.gruppe11_cdio.Objects.MoveDTO;
import com.example.gruppe11_cdio.Objects.Result;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

public class DAO {
    private Retrofit retrofit;
    private CallService service;
    private String url = "http://cdio.isik.dk:8080";

    private Response<MoveDTO> moveResp;
    private Response<GameBoard> gameBoardResp;

    public DAO(){
        this.retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(url)
                .build();
        this.service = retrofit.create(CallService.class);
    }

    public Result<MoveDTO> generateMove(GameBoard gameBoard){
        try {
            Call<MoveDTO> call = service.generateMove(gameBoard);
            moveResp = call.execute();
            if (moveResp.code() == 200) return new Result.Success<MoveDTO>(moveResp.body());
            if (moveResp.code() == 406) return new Result.Success<MoveDTO>(moveResp.body());
            return new Result.Error(new IOException(moveResp.message()));
        } catch (IOException e){
            e.printStackTrace();
            return new Result.Error(new IOException("Fejl i forbindelse med Back End."));
        }
    }

    public Result<MoveDTO> generateGameBoard(ImgDTO imgDTO){
        try {
            Call<GameBoard> call = service.generateGameboard(imgDTO);
            gameBoardResp = call.execute();
            if (gameBoardResp.code() == 200) return new Result.Success<GameBoard>(gameBoardResp.body());
            return new Result.Error(new IOException(gameBoardResp.message()));
        } catch (IOException e){
            e.printStackTrace();
            return new Result.Error(new IOException("Fejl i forbindelse med Back End."));
        }
    }


    public interface CallService {
        @POST("/turn/")
        Call<MoveDTO> generateMove(@Body GameBoard gameBoard);

        @POST("/img/")
        Call<GameBoard> generateGameboard(@Body ImgDTO imgDTO);
    }

}
