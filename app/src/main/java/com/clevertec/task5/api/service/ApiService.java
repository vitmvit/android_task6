package com.clevertec.task5.api.service;

import android.widget.Toast;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.clevertec.task5.MapsActivity;
import com.clevertec.task5.api.api.AtmApi;
import com.clevertec.task5.api.api.FilialApi;
import com.clevertec.task5.api.api.InfoboxApi;
import com.clevertec.task5.api.provider.ApiProvider;
import com.clevertec.task5.dagger.component.DaggerApiComponent;
import com.clevertec.task5.model.dto.AtmDto;
import com.clevertec.task5.model.dto.FilialDto;
import com.clevertec.task5.model.dto.InfoboxDto;
import com.clevertec.task5.model.dto.parent.ApiData;
import com.clevertec.task5.model.pojo.Marker;
import com.clevertec.task5.util.MarkerUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.clevertec.task5.constants.Constants.*;

public class ApiService extends ViewModel {
    private final AtmApi atmApi;
    private final FilialApi filialApi;
    private final InfoboxApi infoboxApi;
    private final List<Marker> markers = new ArrayList<>();
    private MutableLiveData<List<Marker>> listMutableLiveData;

    public ApiService() {
        ApiProvider apiProvider = new ApiProvider(BASE_URL);
        DaggerApiComponent.create().inject(apiProvider);
        atmApi = apiProvider.getRetrofit().create(AtmApi.class);
        filialApi = apiProvider.getRetrofit().create(FilialApi.class);
        infoboxApi = apiProvider.getRetrofit().create(InfoboxApi.class);
    }

    public LiveData<List<Marker>> getMarkers(GoogleMap googleMap, double lat, double lon) {

        if (listMutableLiveData == null) {
            listMutableLiveData = new MutableLiveData<List<Marker>>();
            getCameraStartPosition(googleMap, lat, lon);
            loadMarkers();
        }
        return listMutableLiveData;
    }

    private void getCameraStartPosition(GoogleMap googleMap, double lat, double lon) {
        GoogleMap mMap = googleMap;
        LatLng selectPoint = new LatLng(lat, lon);

        CameraPosition cameraPosition = CameraPosition.builder()
                .target(selectPoint)
                .zoom(14f)
                .build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), null);
    }

    private void loadMarkers() {

        Observable<List<AtmDto>> call1 = atmApi.getAtm(DEFAULT_CITY);
        Observable<List<FilialDto>> call2 = filialApi.getFilial(DEFAULT_CITY);
        Observable<List<InfoboxDto>> call3 = infoboxApi.getInfobox(DEFAULT_CITY);

        Observable
                .merge(call1, call2, call3)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<? extends ApiData>>() {

                    @Override
                    public void onSubscribe(@NotNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NotNull List<? extends ApiData> apiDataList) {
                        for (ApiData apiData : apiDataList) {
                            markers.add(new Marker(
                                    MarkerUtils.getTypeObject(apiData),
                                    MarkerUtils.getAddressType(apiData),
                                    MarkerUtils.getAddress(apiData),
                                    MarkerUtils.getHouse(apiData),
                                    MarkerUtils.getGpsX(apiData),
                                    MarkerUtils.getGpsY(apiData),
                                    MarkerUtils.findDistance(apiData, DEFAULT_LATITUDE_COORD, DEFAULT_LONGITUDE_COORD)
                            ));
                        }
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                        Toast.makeText(new MapsActivity(), DATA_LOADING_ERROR, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {
                        listMutableLiveData.setValue(markers);
                    }
                });
    }
}
