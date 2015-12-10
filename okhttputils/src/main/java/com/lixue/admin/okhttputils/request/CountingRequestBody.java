package com.lixue.admin.okhttputils.request;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

import java.io.IOException;

import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * 用于计数bytes，多用于上传进度的部分
 * Created by lixue on 2015/12/9.
 */
public class CountingRequestBody extends RequestBody {
    protected RequestBody delegate;
    protected Listener listener;
    protected CountingSink countingSink;

    public CountingRequestBody(RequestBody delegate, Listener listener) {
        this.delegate = delegate;
        this.listener = listener;
    }

    @Override
    public MediaType contentType() {
        return delegate.contentType();
    }

    @Override
    public long contentLength() {
        return delegate.contentLength();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        BufferedSink bufferedSink;
        countingSink = new CountingSink(sink);
        bufferedSink = Okio.buffer(countingSink);
        delegate.writeTo(bufferedSink);
        bufferedSink.flush();
    }

    public interface Listener {
        void onRequestProgress(long bytesWritten, long contentLength);
    }

    protected final class CountingSink extends ForwardingSink{
        private long bytesWritten = 0;

        public CountingSink(Sink delegate) {
            super(delegate);
        }

        @Override
        public void write(Buffer source, long byteCount) throws IOException {

            bytesWritten += byteCount;
            listener.onRequestProgress(bytesWritten,contentLength());
        }
    }
}