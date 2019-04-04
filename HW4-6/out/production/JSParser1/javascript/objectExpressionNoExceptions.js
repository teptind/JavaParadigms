function Const(x) {
    this.value = x;
}

Const.prototype = {
    evaluate: function () {
        return this.value;
    },
    toString: function () {
        return this.value.toString();
    },
    diff: function () {
        return Const.ZERO;
    }
};
Const.ZERO = new Const(0);
Const.ONE = new Const(1);

function Variable(vName) {
    this.num = VARS[vName];
    this.name = vName;
}

Variable.prototype = {
    evaluate: function (...values) {
        return values[this.num];
    },
    toString: function () {
        return this.name;
    },
    diff: function (vName) {
        return vName === this.name ? Const.ONE : Const.ZERO;
    }
};

function operationGen(opName, f, fDiff) {
    function Operation(...values) {
        this.args = [...values];
    }

    Operation.prototype = {
        evaluate: function (...values) {
            let res = this.args.map(arg => arg.evaluate(...values));
            return f(...res);
        },
        toString: function () {
            return this.args.join(" ") + " " + opName;
        },
        diff: function (vName) {
            return fDiff(vName, this.args);
        },
    };
    return Operation;
}

let Add = operationGen("+", (a, b) => a + b, (vName, expr) => {
    return new Add(expr[0].diff(vName), expr[1].diff(vName));
});

let Subtract = operationGen("-", (a, b) => a - b, (vName, expr) => {
    return new Subtract(expr[0].diff(vName), expr[1].diff(vName));
});

let Multiply = operationGen("*", (a, b) => a * b, (vName, expr) => {
    return new Add(
        new Multiply(expr[0].diff(vName), expr[1]),
        new Multiply(expr[0], expr[1].diff(vName))
    );
});

// (f / g)' = (f'g - fg') / g^2
let Divide = operationGen("/", (a, b) => a / b, (vName, expr) => {
    return new Divide(
        new Subtract(
            new Multiply(expr[0].diff(vName), expr[1]),
            new Multiply(expr[0], expr[1].diff(vName))
        ),
        new Multiply(expr[1], expr[1])
    );
});

let Negate = operationGen("negate", x => -x, (vName, expr) => {
    return new Negate(expr[0].diff(vName));
});

// (atan(f))' = f' / (f^2 + 1)
let ArcTan = operationGen("atan", x => Math.atan(x), (vName, expr) => {
    return new Divide(
        expr[0].diff(vName),
        new Add(
            new Multiply(expr[0], expr[0]),
            Const.ONE
        )
    );
});

// (atan2(f, g))' = (gf' - fg') / (f^2 + g^2)
let ArcTan2 = operationGen("atan2", (y, x) => Math.atan2(y, x), (vName, expr) => {
    return new Divide(
        new Subtract(
            new Multiply(expr[0].diff(vName), expr[1]),
            new Multiply(expr[0], expr[1].diff(vName))
        ),
        new Add(
            new Multiply(expr[0], expr[0]),
            new Multiply(expr[1], expr[1])
        )
    );
});


const parse = expr => {
    const tokens = expr.split(" ").filter(str => str.length > 0);
    let st = [];
    for (const token of tokens) {
        if (token in OPERATIONS) {
            let [op, numArgs] = OPERATIONS[token];
            st.push(new op(...st.splice(st.length - numArgs, numArgs)));
        } else if (token in CREATED_VARS) {
            st.push(CREATED_VARS[token]);
        } else {
            st.push(new Const(Number(token)));
        }
    }
    return st.pop();
};

const VARS = {
    x: 0,
    y: 1,
    z: 2,
    hello: 0
};

let CREATED_VARS = {};
for (let curVar in VARS) {
    CREATED_VARS[curVar] = new Variable(curVar);
}

const OPERATIONS = {
    "+": [Add, 2],
    "-": [Subtract, 2],
    "*": [Multiply, 2],
    "/": [Divide, 2],
    "negate": [Negate, 1],
    "atan": [ArcTan, 1],
    "atan2": [ArcTan2, 2],
};

let exp = new Multiply(new Const(4), new Const(0)).evaluate(0.0, 0.0, 0.0);
console.log(exp.toString());