package com.pixbits.lib.workflow;

import java.util.function.Consumer;

public interface Sink<T extends WorkflowData> extends Consumer<T> { }
