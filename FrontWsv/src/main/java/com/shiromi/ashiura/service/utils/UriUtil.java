package com.shiromi.ashiura.service.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
public class UriUtil {

    @Value("${url.api}")
    private String urlApi;
    @Value("${url.py}")
    private String urlPy;

    //URI 빌드하다가 귀찮아서 만든 Util
    public URI uriApi(String mapping) {
        return UriComponentsBuilder
                .fromUriString(urlApi)
                .path(mapping)
                .encode()
                .build()
                .toUri();
    }

    public URI uriApi(String mapping,String var1, String var2) {
        return UriComponentsBuilder
                .fromUriString(urlApi)
                .path(mapping+"/{var1}/{var2}")
                .encode()
                .build()
                .expand(var1,var2)
                .toUri();
    }

    public URI uriPy(String mapping) {
        return UriComponentsBuilder
                .fromUriString(urlPy)
                .path(mapping)
                .encode()
                .build()
                .toUri();
    }

    public URI uriPy(String mapping,String var1, String var2) {
        return UriComponentsBuilder
                .fromUriString(urlPy)
                .path(mapping+"/{var1}/{var2}")
                .encode()
                .build()
                .expand(var1,var2)
                .toUri();
    }


}
