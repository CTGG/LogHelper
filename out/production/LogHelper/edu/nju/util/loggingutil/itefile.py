import os
import collections

# 30539 lines of logging stmts
def getStmts():
    path = "/Users/chentiange/Downloads/projects"
    files = os.listdir(path)

    s = []

    for file in files:
        if not os.path.isdir(file):
            f = open(path+"/"+file)
            iter_f = iter(f)
            for line in iter_f:
                s.append(line)

    return s
    # 30539
    # print(len(s))

# 18 types: InitializerDeclaration', ' TryStmt', ' CatchClause', ' LambdaExpr', ' ForeachStmt', ' SwitchStmt', ' BlockStmt', ' WhileStmt', ' MethodCallExpr', ' ConstructorDeclaration', ' ObjectCreationExpr', ' SynchronizedStmt', ' DoStmt', ' ForStmt', ' IfStmt', ' SwitchEntryStmt', ' VariableDeclarator', ' MethodDeclaration'
def getTypes(s):
    types = set()
    for stmt in s:
        type = stmt.split(',')[2]
        types.add(type)
    return types

# dicts of InitializerDeclaration:info InitializerDeclaration:error
def typeWithLevel(s):
    dicts = {}
    for stmt in s:
        level = stmt.split(',')[1]
        type = stmt.split(',')[2]
        if (type,level) in dicts.keys():
            dicts[type,level] = dicts[type,level]+1
        else:
            dicts[type, level] = 1

    return dicts

# ((' BlockStmt', ' Error'), 3)
# ((' BlockStmt', ' WARN'), 2)
# ((' BlockStmt', ' Warn'), 1)
# ((' BlockStmt', ' debug'), 60)
# ((' BlockStmt', ' error'), 39)
# ((' BlockStmt', ' info'), 74)
# ((' BlockStmt', ' trace'), 13)
# ((' BlockStmt', ' warn'), 27)
# ((' CatchClause', ' ERROR'), 10)
# ((' CatchClause', ' Error'), 505)
# ((' CatchClause', ' FINE'), 16)
# ((' CatchClause', ' INFO'), 17)
# ((' CatchClause', ' Info'), 2)
# ((' CatchClause', ' SEVERE'), 55)
# ((' CatchClause', ' TRACE'), 3)
# ((' CatchClause', ' Trace'), 1)
# ((' CatchClause', ' WARN'), 54)
# ((' CatchClause', ' Warn'), 1)
# ((' CatchClause', ' debug'), 431)
# ((' CatchClause', ' error'), 1948)
# ((' CatchClause', ' fatal'), 9)
# ((' CatchClause', ' info'), 740)
# ((' CatchClause', ' trace'), 101)
# ((' CatchClause', ' warn'), 1634)
# ((' ConstructorDeclaration', ' DEBUG'), 1)
# ((' ConstructorDeclaration', ' Debug'), 3)
# ((' ConstructorDeclaration', ' Error'), 1)
# ((' ConstructorDeclaration', ' INFO'), 2)
# ((' ConstructorDeclaration', ' Info'), 3)
# ((' ConstructorDeclaration', ' Trace'), 1)
# ((' ConstructorDeclaration', ' debug'), 82)
# ((' ConstructorDeclaration', ' error'), 1)
# ((' ConstructorDeclaration', ' info'), 124)
# ((' ConstructorDeclaration', ' trace'), 10)
# ((' ConstructorDeclaration', ' warn'), 2)
# ((' DoStmt', ' debug'), 2)
# ((' DoStmt', ' info'), 6)
# ((' DoStmt', ' trace'), 4)
# ((' ForStmt', ' Error'), 1)
# ((' ForStmt', ' Info'), 1)
# ((' ForStmt', ' TRACE'), 1)
# ((' ForStmt', ' debug'), 57)
# ((' ForStmt', ' error'), 8)
# ((' ForStmt', ' info'), 235)
# ((' ForStmt', ' trace'), 11)
# ((' ForStmt', ' warn'), 5)
# ((' ForeachStmt', ' DEBUG'), 1)
# ((' ForeachStmt', ' ERROR'), 2)
# ((' ForeachStmt', ' Error'), 4)
# ((' ForeachStmt', ' INFO'), 1)
# ((' ForeachStmt', ' Info'), 11)
# ((' ForeachStmt', ' Trace'), 1)
# ((' ForeachStmt', ' debug'), 151)
# ((' ForeachStmt', ' error'), 10)
# ((' ForeachStmt', ' info'), 353)
# ((' ForeachStmt', ' trace'), 35)
# ((' ForeachStmt', ' warn'), 25)
# ((' IfStmt', ' DEBUG'), 17)
# ((' IfStmt', ' Debug'), 5)
# ((' IfStmt', ' ERROR'), 16)
# ((' IfStmt', ' Error'), 119)
# ((' IfStmt', ' FINE'), 8)
# ((' IfStmt', ' Fatal'), 1)
# ((' IfStmt', ' INFO'), 27)
# ((' IfStmt', ' Info'), 24)
# ((' IfStmt', ' SEVERE'), 17)
# ((' IfStmt', ' TRACE'), 18)
# ((' IfStmt', ' Trace'), 1)
# ((' IfStmt', ' WARN'), 23)
# ((' IfStmt', ' Warn'), 9)
# ((' IfStmt', ' debug'), 4126)
# ((' IfStmt', ' error'), 1087)
# ((' IfStmt', ' fatal'), 12)
# ((' IfStmt', ' info'), 2889)
# ((' IfStmt', ' trace'), 1050)
# ((' IfStmt', ' warn'), 1994)
# ((' InitializerDeclaration', ' DEBUG'), 6)
# ((' InitializerDeclaration', ' ERROR'), 3)
# ((' InitializerDeclaration', ' FATAL'), 3)
# ((' InitializerDeclaration', ' INFO'), 3)
# ((' InitializerDeclaration', ' TRACE'), 9)
# ((' InitializerDeclaration', ' WARN'), 3)
# ((' InitializerDeclaration', ' debug'), 15)
# ((' InitializerDeclaration', ' info'), 3)
# ((' LambdaExpr', ' Error'), 1)
# ((' LambdaExpr', ' SEVERE'), 1)
# ((' LambdaExpr', ' debug'), 53)
# ((' LambdaExpr', ' error'), 33)
# ((' LambdaExpr', ' info'), 79)
# ((' LambdaExpr', ' trace'), 8)
# ((' LambdaExpr', ' warn'), 22)
# ((' MethodCallExpr', ' debug'), 10)
# ((' MethodCallExpr', ' error'), 8)
# ((' MethodCallExpr', ' info'), 5)
# ((' MethodCallExpr', ' trace'), 2)
# ((' MethodCallExpr', ' warn'), 6)
# ((' MethodDeclaration', ' DEBUG'), 67)
# ((' MethodDeclaration', ' Debug'), 42)
# ((' MethodDeclaration', ' ERROR'), 46)
# ((' MethodDeclaration', ' Error'), 66)
# ((' MethodDeclaration', ' FATAL'), 10)
# ((' MethodDeclaration', ' FINE'), 9)
# ((' MethodDeclaration', ' Fatal'), 3)
# ((' MethodDeclaration', ' INFO'), 140)
# ((' MethodDeclaration', ' Info'), 89)
# ((' MethodDeclaration', ' SEVERE'), 13)
# ((' MethodDeclaration', ' TRACE'), 30)
# ((' MethodDeclaration', ' Trace'), 34)
# ((' MethodDeclaration', ' WARN'), 68)
# ((' MethodDeclaration', ' Warn'), 41)
# ((' MethodDeclaration', ' debug'), 1286)
# ((' MethodDeclaration', ' error'), 312)
# ((' MethodDeclaration', ' fatal'), 10)
# ((' MethodDeclaration', ' info'), 6557)
# ((' MethodDeclaration', ' trace'), 431)
# ((' MethodDeclaration', ' warn'), 306)
# ((' ObjectCreationExpr', ' warn'), 1)
# ((' SwitchEntryStmt', ' debug'), 1)
# ((' SwitchEntryStmt', ' error'), 1)
# ((' SwitchEntryStmt', ' info'), 37)
# ((' SwitchStmt', ' Debug'), 2)
# ((' SwitchStmt', ' Info'), 5)
# ((' SwitchStmt', ' WARN'), 1)
# ((' SwitchStmt', ' Warn'), 2)
# ((' SwitchStmt', ' debug'), 29)
# ((' SwitchStmt', ' error'), 23)
# ((' SwitchStmt', ' fatal'), 5)
# ((' SwitchStmt', ' info'), 82)
# ((' SwitchStmt', ' trace'), 13)
# ((' SwitchStmt', ' warn'), 47)
# ((' SynchronizedStmt', ' debug'), 19)
# ((' SynchronizedStmt', ' error'), 2)
# ((' SynchronizedStmt', ' info'), 23)
# ((' SynchronizedStmt', ' trace'), 5)
# ((' SynchronizedStmt', ' warn'), 1)
# ((' TryStmt', ' DEBUG'), 3)
# ((' TryStmt', ' Debug'), 2)
# ((' TryStmt', ' ERROR'), 1)
# ((' TryStmt', ' FINE'), 2)
# ((' TryStmt', ' INFO'), 22)
# ((' TryStmt', ' Info'), 21)
# ((' TryStmt', ' SEVERE'), 2)
# ((' TryStmt', ' TRACE'), 3)
# ((' TryStmt', ' Trace'), 4)
# ((' TryStmt', ' WARN'), 3)
# ((' TryStmt', ' debug'), 339)
# ((' TryStmt', ' error'), 23)
# ((' TryStmt', ' fatal'), 2)
# ((' TryStmt', ' info'), 1165)
# ((' TryStmt', ' trace'), 119)
# ((' TryStmt', ' warn'), 35)
# ((' VariableDeclarator', ' trace'), 1)
# ((' WhileStmt', ' Warn'), 4)
# ((' WhileStmt', ' debug'), 56)
# ((' WhileStmt', ' error'), 4)
# ((' WhileStmt', ' info'), 231)
# ((' WhileStmt', ' trace'), 16)
# ((' WhileStmt', ' warn'), 5)

# ['info', 'debug', 'trace', 'warn', 'error', 'severe', 'fatal', 'fine']
def getSeq(type):
    s = getStmts()
    dicts = typeWithLevel(s)
    levelfreqpair = {}
    od = collections.OrderedDict(sorted(dicts.items()))
    for k, v in od.iteritems():
        if type in k[0].lower():
            if k[1].lower() not in levelfreqpair.keys():
                levelfreqpair[k[1].lower()] = v
            else:
                levelfreqpair[k[1].lower()] = levelfreqpair[k[1].lower()] + v
    odlevelfreqpair = sorted(levelfreqpair.items(), key=lambda d:d[1],reverse=True)
    res = []
    for pair in odlevelfreqpair:
        res.append(pair[0][1:])
    return res





if __name__=="__main__":
    # s = getStmts()
    # dicts = typeWithLevel(s)
    # od = collections.OrderedDict(sorted(dicts.items()))
    # for k, v in od.iteritems():
    #     print(k[0], v)
    print getSeq('try')



