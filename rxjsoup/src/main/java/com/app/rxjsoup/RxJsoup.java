package com.app.rxjsoup;

import org.jetbrains.annotations.NotNull;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RxJsoup {

    public static Observable<Connection.Response> connect(final Connection jsoupConnection) {
        return Observable.create(new ObservableOnSubscribe<Connection.Response>() {
            @Override
            public void subscribe(ObservableEmitter<Connection.Response> observableEmitter) throws Exception {
                try {
                    final Connection.Response response = jsoupConnection.execute();
                    observableEmitter.onNext(response);
                    observableEmitter.onComplete();
                } catch (Exception e) {
                    observableEmitter.onError(e);
                }
            }

        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
    }

    public static OkHttpClient okHttpClient() {
        return new OkHttpClient();
    }

    public static Request.Builder request() {
        return new Request.Builder();
    }

    public static OkHttpClient.Builder okHttpClientBuilder() {
        return new OkHttpClient.Builder();
    }

    public static Observable<Response> request(final Request request) {
        return Observable.create(new ObservableOnSubscribe<Response>() {
            @Override
            public void subscribe(ObservableEmitter<Response> observableEmitter) throws Exception
            {
                try {
                    Response execute = okHttpClient().newCall(request).execute();
                    observableEmitter.onNext(execute);
                    observableEmitter.onComplete();
                } catch (Exception e) {
                    observableEmitter.onError(e);
                }
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<Response> request(final Request request, final OkHttpClient client) {
        return Observable.create(new ObservableOnSubscribe<Response>() {
            @Override
            public void subscribe(ObservableEmitter<Response> observableEmitter) throws Exception
            {
                try {
                    Response execute = client.newCall(request).execute();
                    observableEmitter.onNext(execute);
                    observableEmitter.onComplete();
                } catch (Exception e) {
                    observableEmitter.onError(e);
                }
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<Document> requestDocument(final Request request) {
        return Observable.create(new ObservableOnSubscribe<Document>() {
            @Override
            public void subscribe(ObservableEmitter<Document> observableEmitter) throws Exception
            {
                try {
                    Response execute = okHttpClient().newCall(request).execute();
                    Document document = Jsoup.parse(execute.body().string());
                    observableEmitter.onNext(document);
                    observableEmitter.onComplete();
                } catch (Exception e) {
                    observableEmitter.onError(e);
                }
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<String> requestHtml(final Request request) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> observableEmitter) throws Exception
            {
                try {
                    Response execute = okHttpClient().newCall(request).execute();
                    Document document = Jsoup.parse(execute.body().string());
                    observableEmitter.onNext(document.html());
                    observableEmitter.onComplete();
                } catch (Exception e) {
                    observableEmitter.onError(e);
                }
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<Element> requestElement(final Request request,
            @NotNull final String expression)
    {
        return Observable.create(new ObservableOnSubscribe<Element>() {
            @Override
            public void subscribe(ObservableEmitter<Element> observableEmitter) throws Exception
            {
                try {
                    Response execute = okHttpClient().newCall(request).execute();
                    Document document = Jsoup.parse(execute.body().string());
                    final Elements elements = document.select(expression);
                    if (elements.isEmpty()) {
                        observableEmitter.onError(
                                new NotFoundException(expression, elements.toString()));
                    } else {
                        for (Element elem : elements) {
                            observableEmitter.onNext(elem);
                        }
                    }
                    observableEmitter.onComplete();
                } catch (Exception e) {
                    observableEmitter.onError(e);
                }
            }
        }).subscribeOn(Schedulers.newThread());
    }


    public static Observable<String> attr(final Element element, final String expression,
            final String attr)
    {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> observableEmitter) throws Exception {
                final Elements elements = element.select(expression);
                if (elements.isEmpty()) {
                    observableEmitter.onError(new NotFoundException(expression, element.toString()));
                } else {
                    if (elements.isEmpty()) {
                        observableEmitter.onNext("");
                    } else {
                        for (Element e : elements) {
                            observableEmitter.onNext(e.attr(attr));
                        }
                    }
                    observableEmitter.onComplete();
                }
            }
        });
    }

    public static Observable<String> href(Element element, String expression) {
        return attr(element, expression, "href");
    }

    public static Observable<String> src(Element element, String expression) {
        return attr(element, expression, "src");
    }

    public static Observable<String> html(final Document element) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> observableEmitter) throws Exception {
                observableEmitter.onNext(element.body().html());
                observableEmitter.onComplete();
            }
        });
    }

    public static Observable<String> html(final Response response) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> observableEmitter) throws Exception {
                observableEmitter.onNext(Jsoup.parse(response.body().toString()).body().html());
                observableEmitter.onComplete();
            }
        });
    }

    public static Observable<Document> document(final String response) {
        return Observable.create(new ObservableOnSubscribe<Document>() {
            @Override
            public void subscribe(ObservableEmitter<Document> observableEmitter) throws Exception {
                observableEmitter.onNext(Jsoup.parse(response));
                observableEmitter.onComplete();
            }
        });
    }

    public static Observable<Document> document(final Response response) {
        return Observable.create(new ObservableOnSubscribe<Document>() {
            @Override
            public void subscribe(ObservableEmitter<Document> observableEmitter) throws Exception {
                observableEmitter.onNext(Jsoup.parse(response.body().toString()));
                observableEmitter.onComplete();
            }
        });
    }

    public static Observable<String> text(final Element element, final String expression) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> observableEmitter) throws Exception {
                final Elements elements = element.select(expression);
                if (elements.isEmpty()) {
                    observableEmitter.onError(new NotFoundException(expression, element.toString()));
                } else {
                    if (elements.isEmpty()) {
                        observableEmitter.onNext("");
                    } else {
                        for (Element e : elements) {
                            observableEmitter.onNext(e.text());
                        }
                    }
                    observableEmitter.onComplete();
                }
            }
        });
    }


    public static Observable<Element> getElementsByAttributeValue(final Element element,
            final String key, final String value)
    {
        return Observable.create(new ObservableOnSubscribe<Element>() {
            @Override
            public void subscribe(ObservableEmitter<Element> observableEmitter) throws Exception {
                final Elements elements = element.getElementsByAttributeValue(key, value);
                if (elements.isEmpty()) {
                    observableEmitter.onError(new NotFoundException(key + " " + value, element.toString()));
                } else {
                    for (Element e : elements) {
                        observableEmitter.onNext(e);
                    }
                    observableEmitter.onComplete();
                }
            }
        });
    }

    public static class NotFoundException extends Exception {

        public NotFoundException(String expression, String document) {
            super("`" + expression + "` not found in `" + document + "`");
        }
    }
}